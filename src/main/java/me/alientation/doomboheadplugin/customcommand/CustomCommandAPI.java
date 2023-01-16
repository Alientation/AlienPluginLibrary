package me.alientation.doomboheadplugin.customcommand;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import me.alientation.doomboheadplugin.customcommand.annotations.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

/**
 * Class to be extended to contain annotated commands
 */
public class CustomCommandAPI {
	private final Map<String,Method> methodMap;
	private CustomCommandManager commandManager;

	/**
	 * Default constructor
	 */
	public CustomCommandAPI() {
		this.methodMap = new HashMap<>();
	}

	/**
	 * Constructs with command manager
	 *
	 * @param manager command manager
	 */
	public CustomCommandAPI(CustomCommandManager manager) {
		this.methodMap = new HashMap<>();
		registerManager(manager);
	}

	/**
	 * Registers command manager
	 *
	 * @param manager Custom command manager
	 */
	public void registerManager(CustomCommandManager manager) {
		this.commandManager = manager;
	}

	/**
	 * Registers the annotated command methods in this class
	 */
	public void registerMethods() {
		for (Method method : this.getClass().getDeclaredMethods()) {
			if (!method.isAnnotationPresent(CommandAnnotation.class) && !method.isAnnotationPresent(CommandTabAnnotation.class)) continue;

			//annotated tab complete method
			if (method.isAnnotationPresent(CommandTabAnnotation.class)) {
				CommandTabAnnotation tabAnnotation = method.getAnnotation(CommandTabAnnotation.class);
				this.methodMap.put("@tabAnnotation@" + tabAnnotation.commandID(), method);
				CustomCommand command = this.getCommand(tabAnnotation.commandID(), tabAnnotation.commandName());
				command.validateTabMethod(method, this);

				System.out.println("Registering Command Tab Method " + this.getCommand(tabAnnotation.commandID(), tabAnnotation.commandName()));
				continue;
			}

			//annotated command method, gets annotations
			CommandAnnotation commandAnnotation = method.getAnnotation(CommandAnnotation.class);
			CommandAliasAnnotation[] aliasesAnnotations = method.getAnnotationsByType(CommandAliasAnnotation.class);
			CommandDescriptionAnnotation descriptionAnnotation = method.getAnnotation(CommandDescriptionAnnotation.class);
			PermissionAnnotation[] permissionAnnotations = method.getAnnotationsByType(PermissionAnnotation.class);
			CommandUsageAnnotation usageAnnotation = method.getAnnotation(CommandUsageAnnotation.class);

			//loads command name and id from annotation todo throw error if they arent present
			String commandName = commandAnnotation.commandName();
			String commandID = commandAnnotation.commandID();

			//loads aliases from annotation
			List<String> commandAliases = new ArrayList<>();
			for (CommandAliasAnnotation aliasesAnnotation : aliasesAnnotations) commandAliases.add(aliasesAnnotation.value());

			//loads the command description from annotation
			String commandDescription = descriptionAnnotation != null ? descriptionAnnotation.value() : null;

			//loads command permission from annotation
			List<String> commandPermissions = new ArrayList<>();
			List<Boolean> commandRequiredPermissions = new ArrayList<>();
			for (PermissionAnnotation permissionAnnotation : permissionAnnotations) {
				commandPermissions.add(permissionAnnotation.permission());
				commandRequiredPermissions.add(permissionAnnotation.required());
			}

			//loads command usage from annotations
			String commandUsage = usageAnnotation != null ? usageAnnotation.value() : null;

			//maps method to command pathway
			this.methodMap.put("@commandAnnotation@" + commandAnnotation.commandID(), method);

			//instantiates the custom command
			CustomCommand command = this.getCommand(commandID, commandName);

			for (int i = 0; i < commandPermissions.size(); i++)
				command.addPermission(commandPermissions.get(i),commandRequiredPermissions.get(i));

			System.out.println("Registering Command Method " + this.getCommand(commandID, commandName));

			if (command.isParent()) {
				System.out.println("^ is a parent command");
				Field bukkitCommandMap;
				try {
					bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
					bukkitCommandMap.setAccessible(true);
					CommandMap commandMap = ((CommandMap) bukkitCommandMap.get(Bukkit.getServer()));
					commandMap.register(commandName, new BaseCommand(commandName,commandDescription, commandUsage, commandAliases, command));

					if (this.commandManager.getPlugin().getCommand(commandName) != null)
						Objects.requireNonNull(this.commandManager.getPlugin().getCommand(commandName)).unregister(commandMap);

				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			command.validateCommandMethod(method,this);
		}
	}

	/**
	 * Gets the command for the given search parameters
	 *
	 * @param commandID	command pathway
	 * @return Custom Command
	 */
	private CustomCommand getCommand(String commandID, String commandName) {
		CustomCommand command = this.commandManager.getCustomCommandMap().get(commandID);

		if (command == null) {
			command = CustomCommand.Builder.newInstance()
					.id(commandID)
					.name(commandName)
					.manager(this.commandManager)
					.build();
			this.commandManager.mapCommand(command);
		}

		return command;
	}

	/**
	 * Gets the method mapping
	 *
	 * @return method map
	 */
	public Map<String,Method> getMethodMap() {
		return this.methodMap;
	}
}