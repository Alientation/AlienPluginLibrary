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

	//mapping of the command id to the command object
	private final Map<String,CustomCommand> CUSTOM_COMMAND_MAP;

	//plugin where the commands are situated in
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
		//maps the command
		this.CUSTOM_COMMAND_MAP.put(command.getId(),command);

		//breaks the unique identifier (help.list -> [help,list]
		String[] parts = command.getId().split("\\.");
		System.out.println(command.getId() + " \n" + Arrays.toString(parts));

		//current command pathway
		StringBuilder cmdPath = new StringBuilder(parts[0]);

		//builds missing parts of the command tree (parent and children)
		//gets custom command at current pathway
		CustomCommand head = this.CUSTOM_COMMAND_MAP.get(cmdPath.toString());

		//if the custom command hasn't been initiated, initialize it
		if (head == null) {
			head = CustomCommand.Builder.newInstance().id(cmdPath.toString()).name(cmdPath.toString()).manager(this).build();
			this.CUSTOM_COMMAND_MAP.put(head.getId(), head);
		}

		//initialize children commands if not already initialized
		CustomCommand child;
		for (int i = 1; i < parts.length; i++) {
			cmdPath.append(".").append(parts[i]);

			//getting child
			child = this.CUSTOM_COMMAND_MAP.get(cmdPath.toString());

			//if child does not exist yet
			if (child == null) {
				//creates and adds child command
				child = CustomCommand.Builder.newInstance().id(cmdPath.toString()).name(parts[i]).manager(this).build();
				this.CUSTOM_COMMAND_MAP.put(child.getId(), child);
			}

			//moving down the link
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
		//todo figure out if the plugin is a necessary component
		if (this.plugin == null) throw new PluginNotFoundException("The plugin has not yet been registered to the manager");

		//iterate through each command already loaded in the map and register them
		this.CUSTOM_COMMAND_MAP.forEach((commandID,customCommand) -> {
			//not the parent command (it by itself is not a complete command, there are arguments to it that are required)
			if (!customCommand.isParent()) return;

			//registering command to the bukkit command map
			System.out.println("COMMAND >>> " + customCommand.getName());

			Field bukkitCommandMap;
			try {
				bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap"); //Bukkit's command mappings
				bukkitCommandMap.setAccessible(true);
				CommandMap commandMap = ((CommandMap) bukkitCommandMap.get(Bukkit.getServer()));

				//not the correct command type
				if (!(commandMap.getCommand(commandID) instanceof BaseCommand)) { //todo throw error instead
					System.out.println(commandID + " is an invalid command as it isn't an instance of BaseCommand");
					return;
				}

				//registers the executing command method to the command
				((BaseCommand) Objects.requireNonNull(commandMap.getCommand(commandID))).setExecutor(customCommand);
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
