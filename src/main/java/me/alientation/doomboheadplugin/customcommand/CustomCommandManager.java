package me.alientation.doomboheadplugin.customcommand;

import java.lang.reflect.Field;
import java.util.*;

import me.alientation.doomboheadplugin.customcommand.exceptions.PluginNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Manages CustomCommands
 */
public class CustomCommandManager {
	
	private final Map<String,CustomCommand> CUSTOM_COMMAND_MAP;
	private final JavaPlugin plugin;
	
	/**
	 * Constructor that initiates Java Reflection to map each command to a method
	 * 
	 * @param plugin Plugin to be associated with the CustomCommandManager
	 */
	public CustomCommandManager(JavaPlugin plugin) {
		this.CUSTOM_COMMAND_MAP = new HashMap<>();
		this.plugin = plugin;
	}

	/**
	 * Registers the command to the manager
	 * 
	 * @param command The target command to be registered
	 */
	public void mapCommand(CustomCommand command) {
		this.CUSTOM_COMMAND_MAP.put(command.getId(),command); //maps the command

		String[] parts = command.getId().split("\\."); //breaks the unique identifier (help.list -> [help,list]
		System.out.println(command.getId() + " \n" + Arrays.toString(parts));

		StringBuilder cmdPath = new StringBuilder(parts[0]); //current command pathway

		//builds missing parts of the command tree (parent and children)
		CustomCommand head = this.CUSTOM_COMMAND_MAP.get(cmdPath.toString()); //gets custom command at current pathway
		if (head == null) { //if the custom command hasn't been initiated, initialize it
			head = CustomCommand.Builder.newInstance().id(cmdPath.toString()).name(cmdPath.toString()).manager(this).build();
			this.CUSTOM_COMMAND_MAP.put(head.getId(), head);
		}

		CustomCommand child; //initialize children commands if not already initialized
		for (int i = 1; i < parts.length; i++) {
			cmdPath.append(".").append(parts[i]);

			child = this.CUSTOM_COMMAND_MAP.get(cmdPath.toString());

			if (child == null) {
				//creates and adds child command
				child = CustomCommand.Builder.newInstance().id(cmdPath.toString()).name(parts[i]).manager(this).build();
				this.CUSTOM_COMMAND_MAP.put(child.getId(), child);
			}

			head.addChildCommand(child);
			child.setParent(head);
			head = child;
		}
	}
	
	/**
	 * Sets up command executors for the plugin
	 * 
	 * @param commands	An object to the class that contains the command methods
	 */
	public void loadCommand(@NotNull CustomCommandAPI commands) {
		commands.registerManager(this);
		commands.registerMethods();
	}

	/**
	 * Registers commands associated in the custom command map using reflection to link with Bukkit's command map
	 */
	public void registerCommand() throws PluginNotFoundException {
		if (this.plugin == null) throw new PluginNotFoundException("The plugin has not yet been registered to the manager");

		this.CUSTOM_COMMAND_MAP.forEach((key,value) -> {
			if (!value.isParent()) return;

			//System.out.println("COMMAND >>> " + value.getCommandName());
			Field bukkitCommandMap;
			try {
				bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap"); //Bukkit's command mappings
				bukkitCommandMap.setAccessible(true);
				CommandMap commandMap = ((CommandMap) bukkitCommandMap.get(Bukkit.getServer()));

				if (!(commandMap.getCommand(key) instanceof BaseCommand)) { //todo throw error instead
					System.out.println(key + " is an invalid command as it isn't an instance of BaseCommand");
					return;
				}

				((BaseCommand) Objects.requireNonNull(commandMap.getCommand(key))).setExecutor(value);

			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		});
	}

	/**
	 * Gets the command mapping
	 *
	 * @return custom command map
	 */
	public Map<String,CustomCommand> getCustomCommandMap() {
		return this.CUSTOM_COMMAND_MAP;
	}

	/**
	 * Gets the Plugin
	 *
	 * @return returns the plugin
	 */
	public JavaPlugin getPlugin() {
		return this.plugin;
	}
}