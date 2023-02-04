package me.alientation.doomboheadplugin.customcommand;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import me.alientation.doomboheadplugin.customcommand.annotations.arguments.Argument;
import me.alientation.doomboheadplugin.customcommand.annotations.arguments.CommandArgumentAnnotation;
import me.alientation.doomboheadplugin.customcommand.annotations.arguments.CommandArgumentCustomConditionAnnotation;
import me.alientation.doomboheadplugin.customcommand.annotations.arguments.CustomArgument;
import me.alientation.doomboheadplugin.customcommand.annotations.commands.CommandAnnotation;
import me.alientation.doomboheadplugin.customcommand.annotations.commands.CommandTabAnnotation;
import me.alientation.doomboheadplugin.customcommand.annotations.permissions.PermissionAnnotation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

/**
 * Class to be extended to contain annotated commands
 * Note: All command on tab complete methods should be in the same class as the command execute method
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

		//looks for command declarations before checking tab complete
		for (Method method : this.getClass().getDeclaredMethods()) {
			if (!method.isAnnotationPresent(CommandAnnotation.class)) continue;


			//annotated command arguments
			CommandArgumentAnnotation[] commandArgumentAnnotations = method.getAnnotationsByType(CommandArgumentAnnotation.class);
			Argument[] arguments = new Argument[commandArgumentAnnotations.length];
			registerCommandArguments(commandArgumentAnnotations, arguments);


			//annotated command method, gets annotations
			CommandAnnotation commandAnnotation = method.getAnnotation(CommandAnnotation.class);
			PermissionAnnotation[] permissionAnnotations = method.getAnnotationsByType(PermissionAnnotation.class);

			String commandName = commandAnnotation.name();
			String commandID = commandAnnotation.id();

			List<String> commandAliases = new ArrayList<>(Arrays.asList(commandAnnotation.aliases()));
			String commandDescription = commandAnnotation.description();
			String commandUsage = commandAnnotation.usage();

			List<String> commandPermissions = new ArrayList<>();
			List<String> requiredCommandPermissions = new ArrayList<>();
			for (PermissionAnnotation permissionAnnotation : permissionAnnotations) {
				if (permissionAnnotation.required())
					requiredCommandPermissions.add(permissionAnnotation.permission());
				commandPermissions.add(permissionAnnotation.permission());
			}


			//maps reflection method to command id
			this.methodMap.put("@commandAnnotation@" + commandAnnotation.id(), method);


			CustomCommand command;

			//command already exists
			if (commandManager.getCustomCommand(commandID) != null) {
				command = commandManager.getCustomCommand(commandID);

				command.setDescription(commandDescription);
				command.setUsage(commandUsage);
				command.clearAliases();
				for (String alias : commandAliases) command.addAlias(alias);
				command.setArguments(arguments);
				command.clearPermissions();
				command.clearRequiredPermissions();
				for (String permission : commandPermissions) command.addPermission(permission);
				for (String requiredPermission : requiredCommandPermissions) command.addRequiredPermission(requiredPermission);

			} else {
				//build command
				CustomCommand.Builder builder = CustomCommand.Builder.newInstance();
				builder.manager(commandManager)
						.id(commandID)
						.name(commandName)
						.description(commandDescription)
						.usage(commandUsage)
						.aliases(commandAliases)
						.arguments(arguments)
						.permissions(commandPermissions)
						.requiredPermissions(requiredCommandPermissions);

				command = builder.build();
			}

			//maps command and registers and children if needed
			this.commandManager.mapCommand(command);

			System.out.println("Registering Command Method " + command);

			//map command to bukkit
			if (command.isParent()) {
				System.out.println("^ is a parent command");
				Field bukkitCommandMap;
				try {

					bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
					bukkitCommandMap.setAccessible(true);
					CommandMap commandMap = ((CommandMap) bukkitCommandMap.get(Bukkit.getServer()));

					//if plugin has another command of the same name, resolve the issues?
					if (this.commandManager.getPlugin().getCommand(commandName) != null) {
						System.out.println("Resolving duplicate command names (" + commandName + ")");
						Objects.requireNonNull(this.commandManager.getPlugin().getCommand(commandName)).unregister(commandMap);
					}

					commandMap.register(commandName, new BaseCommand(commandName,commandDescription, commandUsage, commandAliases, command));

				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			command.validateCommandMethod(method,this);
		}

		//check for on tab complete methods
		for (Method method : this.getClass().getDeclaredMethods()) {
			if (!method.isAnnotationPresent(CommandTabAnnotation.class)) continue;

			//annotated tab complete method
			CommandTabAnnotation tabAnnotation = method.getAnnotation(CommandTabAnnotation.class);
			this.methodMap.put("@tabAnnotation@" + tabAnnotation.id(), method);
			CustomCommand command = this.getCommand(tabAnnotation.id(), tabAnnotation.name());
			command.validateTabMethod(method, this);

			System.out.println("Registering Command Tab Method " + this.getCommand(tabAnnotation.id(), tabAnnotation.name()));
		}

	}

	private void registerCommandArguments(CommandArgumentAnnotation @NotNull [] commandArgumentAnnotations, Argument[] arguments) {
		for (int i = 0; i < commandArgumentAnnotations.length; i++) {
			CommandArgumentAnnotation arg = commandArgumentAnnotations[i];

			if (arg.customCondition().matchConditionClass() == CommandArgumentCustomConditionAnnotation.BASE_CLASS) {
				//not custom argument
				arguments[i] = new Argument(arg.name(),arg.description(),arg.usage(),
						Argument.extractMatchConditions(arg.condition().matchCondition()),
						arg.optional(), arg.condition().checkValidPlayerName(), arg.condition().checkValidInteger(),
						arg.condition().checkValidFloat(), arg.condition().minNumber(), arg.condition().maxNumber());
				continue;
			}

			Method matchConditionMethod = null;
			for (Method checkMethod : arg.customCondition().matchConditionClass().getDeclaredMethods()) {
				if (checkMethod.getName().equals(arg.customCondition().matchConditionMethod())) {
					matchConditionMethod = checkMethod;
					break;
				}
			}

			//custom argument
			arguments[i] = new CustomArgument(arg.name(),arg.description(),arg.usage(),
					arg.customCondition().matchConditionClass(), matchConditionMethod, arg.optional());
		}
	}

	/**
	 * Gets the command for the given search parameters
	 *
	 * @param commandID	command pathway
	 * @return Custom Command
	 */
	private CustomCommand getCommand(String commandID, String commandName) {
		CustomCommand command = this.commandManager.getCustomCommand(commandID);

		if (command == null) {
			command = CustomCommand.Builder.newInstance().name(commandName).id(commandID)
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