package me.alientation.doomboheadplugin.customcommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

import me.alientation.doomboheadplugin.customcommand.annotations.arguments.Argument;
import me.alientation.doomboheadplugin.customcommand.events.CommandCallAttemptEvent;
import me.alientation.doomboheadplugin.customcommand.events.CommandCallSuccessEvent;
import me.alientation.doomboheadplugin.customcommand.exceptions.InvalidMethodException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

/**
 * Class for storing information regarding a CustomCommand and forwarding functionality to reflected methods
 * <p>
 *
 * command ids are stored in this way
 * static names are plainly the parent's id + "." + the command name
 * so for example /help list add has the id help.list.add (for the command add of the help.list parent command)
 * <p>
 * however if the command has parameters instead of ".", there is a "?" + the number of parameters there are for that command
 * for example /help list add ___ has the id help.list.add?1
 */
public class CustomCommand implements CommandExecutor, TabCompleter {
	//A unique identifier for the specific command. For example the command /help list -> help.list
	private final String id;

	//name of the command /help list -> list
	private final String name;

	//description of the command that is displayed
	private String description;

	//usage displayed when the onCommand fails
	private String usage;

	//Aliases to the command
	private final List<String> aliases;

	//permissions for the current command, TODO link it with the CustomPermission
	private final Set<String> permissions;
	private final Set<String> requiredPermissions;

	//arguments for the command
	private final Argument[] arguments;


	//The parent command. For example the command /help list -> help
	private CustomCommand parent;

	//The sub commands of this parent command. All sub commands inherit the same permission requirements as the parent command
	private final Set<CustomCommand> children;

	//the methods linked to the custom command
	private Method commandMethod;
	private Method tabMethod;

	//the classes containing the linked methods
	private CustomCommandAPI commandMethodContainer;
	private CustomCommandAPI tabMethodContainer;

	//BukkitCommand linked to this
	private BaseCommand baseCommand;

	//Command Manager
	private final CustomCommandManager manager;

	//whether to show aliases as possible tab completions
	private boolean showAliasesInTabCompletion;
	private boolean visible;
	private boolean disabled;
	private boolean ignorePermissions;

	public static class Builder {
		private CustomCommandManager manager;
		private String id, name, description, usage;
		private final Collection<String> aliases, permissions, requiredPermissions;
		private Argument[] arguments;
		private boolean showAliasesInTabCompletion, visible, disabled, ignorePermissions;

		private Builder() {
			aliases = new ArrayList<>();
			requiredPermissions = new HashSet<>();
			permissions = new HashSet<>();
			arguments = new Argument[] {};
			showAliasesInTabCompletion = false;
			visible = true;
			disabled = false;
			ignorePermissions = false;
		}
		public static Builder newInstance() {
			return new Builder();
		}

		public Builder manager(CustomCommandManager manager) {
			this.manager = manager;
			return this;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder usage(String usage) {
			this.usage = usage;
			return this;
		}

		public Builder alias(String alias) {
			this.aliases.add(alias);
			return this;
		}

		public Builder aliases(Collection<String> aliases) {
			this.aliases.addAll(aliases);
			return this;
		}

		public Builder permission(String permission) {
			this.permissions.add(permission);
			return this;
		}

		public Builder permissions(Collection<String> permissions) {
			this.permissions.addAll(permissions);
			return this;
		}

		public Builder requiredPermissions(Collection<String> requiredPermissions) {
			this.requiredPermissions.addAll(requiredPermissions);
			return this;
		}

		public Builder requiredPermission(String requiredPermission) {
			this.requiredPermissions.add(requiredPermission);
			return this;
		}

		public Builder arguments(Argument[] arguments) {
			 this.arguments = arguments;
			 return this;
		}

		public Builder showAliasesInTabCompletion(boolean showAliasesInTabCompletion) {
			this.showAliasesInTabCompletion = showAliasesInTabCompletion;
			return this;
		}

		public Builder visible(boolean visible) {
			this.visible = visible;
			return this;
		}

		public Builder disabled(boolean disabled) {
			this.disabled = disabled;
			return this;
		}

		public Builder ignorePermissions(boolean ignorePermissions) {
			this.ignorePermissions = ignorePermissions;
			return this;
		}

		public void verify() {
			if (id == null) throw new IllegalStateException("id can't be null");
			if (name == null) throw new IllegalStateException("name can't be null");
		}

		public CustomCommand build() {
			verify();
			return new CustomCommand(this);
		}
	}

	/**
	 * Constructor using the Builder pattern to load optional attributes
	 *
	 * @param builder Builder class
	 */
	public CustomCommand(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.description = builder.description;
		this.usage = builder.usage;
		this.aliases = (List<String>) builder.aliases;
		this.permissions = new HashSet<>();
		this.requiredPermissions = new HashSet<>();
		this.children = new HashSet<>();

		this.permissions.addAll(builder.permissions);
		this.requiredPermissions.addAll(builder.requiredPermissions);

		this.arguments = builder.arguments;

		this.manager = builder.manager;

		this.showAliasesInTabCompletion = builder.showAliasesInTabCompletion;
		this.visible = builder.visible;
		this.disabled = builder.disabled;
		this.ignorePermissions = builder.ignorePermissions;
	}

	/**
	 * Process command execution
	 *
	 * @param sender source of the command
	 * @param command command which was executed
	 * @param label alias of the command which was used
	 * @param args passed command arguments
	 * @return whether the command was executed
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		CommandCallAttemptEvent commandCallAttemptEvent = new CommandCallAttemptEvent(this);

		//initiates event
		Bukkit.getPluginManager().callEvent(commandCallAttemptEvent);
		System.out.println(command.getName() + " Command called with " + Arrays.toString(args) + " args");

		if (this.disabled) commandCallAttemptEvent.setCancelled(true);
		System.out.println("Cancelled because command is disabled");

		//command call is cancelled
		System.out.println("Checking if command call attempt event is cancelled");
		if (commandCallAttemptEvent.isCancelled()) return !commandCallAttemptFail(sender,command,label,args);

		//sender doesn't have permissions
		System.out.println("Checking if sender has permissions");
		if (!hasPermissions(sender)) return !invalidPermissions(sender,command,label,args);

		//check if command has enough arguments passed
		System.out.println("Checking if sender passed enough arguments");
		if (args.length < arguments.length) return !invalidArgumentCount(sender,command,label,args);

		//check if command arguments pass the condition checks
		System.out.println("Checking if sender passed appropriate arguments"); //todo incorporate optional arguments into these checks (if there is optional arguments, this means there should not be further children
		for (int argIndex = 0; argIndex < arguments.length; argIndex++)
			if (!arguments[argIndex].doesMatchCondition(sender,command,label,args[argIndex])) return !invalidArgument(sender,command,label,args[argIndex], arguments[argIndex]);

		//processes down the argument pathway if there exists a children command
		System.out.println("Checking if there is a child command");
		CustomCommand child = args.length > arguments.length ? getChildrenByName(args[arguments.length]) : null;
		if (child != null) return child.onCommand(sender, command, label, removeFirst(args, 1 + arguments.length));

		//no linked on command method
		System.out.println("Checking if command is invalid");
		if (this.commandMethod == null) return !invalidCommand(sender,command,label,args);

		System.out.println("Passed all checks, proceeding to invoking command");

		//Object[] params = {sender,command,label,args};
		//matches parameters to the command method parameters
		Object[] params = new Object[this.commandMethod.getParameterCount()]; //todo figure out whether to have custom parameter forwarding
		int paramsIndex = 0;
		for (Class<?> c : this.commandMethod.getParameterTypes()) {
			if (c == CommandSender.class)	params[paramsIndex] = sender;
			else if (c == Command.class) 	params[paramsIndex] = command;
			else if (c == String.class) 	params[paramsIndex] = label;
			else if (c == String[].class) 	params[paramsIndex] = args;
			else 							params[paramsIndex] = null;
			paramsIndex++;
		}

		//invoking command method through reflection
		try {
			Bukkit.getPluginManager().callEvent(new CommandCallSuccessEvent(this));
			return (boolean) this.commandMethod.invoke(this.commandMethodContainer,params)
					&& commandCallAttemptSuccess(sender,command,label,args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Process tab completion
	 *
	 * @param sender source of the command.  For players tab-completing a
	 *     command inside a command block, this will be the player, not
	 *     the command block.
	 * @param command command which was executed
	 * @param label alias of the command which was used
	 * @param args the arguments passed to the command, including final
	 *     partial argument to be completed
	 * @return possible tab completions
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		System.out.println("Tab completion for " + command.getName() + " with " + Arrays.toString(args) + " args");
		if (!visible) return new ArrayList<>();

		//there is a custom tab completion for this
		if (this.tabMethod != null) {
			System.out.println("Custom tab completion for this command");

			//matching parameters TODO add parameter flags
			Object[] params = new Object[this.tabMethod.getParameterCount()];
			int paramsIndex = 0;
			for (Class<?> c : this.tabMethod.getParameterTypes()) {
				if (c == CommandSender.class)	params[paramsIndex] = sender;
				else if (c == Command.class) 	params[paramsIndex] = command;
				else if (c == String.class) 	params[paramsIndex] = label;
				else if (c == String[].class) 	params[paramsIndex] = args;
				else 							params[paramsIndex] = null;
				paramsIndex++;
			}

			//invoking custom tab completion method
			try {
				return (List<String>) this.tabMethod.invoke(this.tabMethodContainer,params);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				e.printStackTrace();
			}
		}

		//default tab complete
		if (args.length > 1 + arguments.length) {
			//there are arguments not part of this command, so processing of tab complete shouldn't happen here

			//no child to process tab complete
			System.out.println("Checking child for tab completion");
			CustomCommand child = getChildrenByName(args[arguments.length]);
			if (child == null || !child.isVisible()) return new ArrayList<>();

			//child processes tab complete
			System.out.println("sending tab completion processing to child command");
			return child.onTabComplete(sender, command, label, removeFirst(args,arguments.length + 1));
		}

		//built in tab completion todo take into account of command arguments
		List<String> possibleCompletions = new ArrayList<>();

		//no more command arguments to be passed
		if (args.length == 1 + arguments.length) {
			for (CustomCommand commands : this.children) {
				//adding child command names
				if (commands.hasPermissions(sender) && commands.getName().indexOf(args[0]) == 0)
					possibleCompletions.add(commands.getName());

				//don't show aliases on tab completion
				if (!showAliasesInTabCompletion) continue;

				//adding aliases of the child commands
				for (String s : commands.getAliases())
					if (s.indexOf(args[0]) == 0) possibleCompletions.add(s);
			}
		} else { //command arguments todo incorporate optional arguments into these checks (if there is optional arguments, this means there should not be further children


		}
		System.out.println("possible completions " + possibleCompletions);
		return possibleCompletions;
	}
	
	/**
	 * Called whenever a command issued by a sender does not exist, likely meaning they had incorrect arguments
	 *
	 * @param sender issuer of the command
	 * @param command command that was issued
	 * @param label text that was typed
	 * @param args arguments passed to the command
	 * 
	 * @return whether the command should print out the usage description
	 */
	public boolean invalidCommand(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}
	
	/**
	 * Called whenever a sender does not have the appropriate permissions to execute a command
	 * 
	 * @param sender issuer of the command
	 * @param command command that was issued
	 * @param label text that was typed
	 * @param args arguments passed to the command
	 * 
	 * @return whether the command should print out the usage description
	 */
	public boolean invalidPermissions(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}

	/**
	 * Called whenever a sender did not pass the appropriate arguments to execute a command
	 *
	 * @param sender issuer of the command
	 * @param command command that was issued
	 * @param label text that was typed
	 * @param args arguments passed to the command
	 *
	 * @return whether the command should print out the usage description
	 */
	public boolean invalidArgumentCount(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}

	/**
	 * Called whenever a sender did not pass the appropriate arguments to execute a command
	 *
	 * @param sender issuer of the command
	 * @param command command that was issued
	 * @param label text that was typed
	 * @param arg argument passed to the command
	 * @param requiredArgument the required argument to be passed to command
	 *
	 * @return whether the command should print out the usage description
	 */
	public boolean invalidArgument(CommandSender sender, Command command, String label, String arg, Argument requiredArgument) {
		return true;
	}

	/**
	 * Called whenever a sender does not have the appropriate permissions to execute a command
	 * 
	 * @param sender issuer of the command
	 * @param command command that was issued
	 * @param label text that was typed
	 * @param args arguments passed to the command
	 * 
	 * @return whether the command should print out the usage description
	 */
	public boolean commandCallAttemptFail(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}
	
	/**
	 * Called whenever a sender does not have the appropriate permissions to execute a command
	 * 
	 * @param sender issuer of the command
	 * @param command command that was issued
	 * @param label text that was typed
	 * @param args arguments passed to the command
	 * 
	 * @return whether the command should print out the usage description
	 */
	public boolean commandCallAttemptSuccess(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}
	
	/**
	 * Temporary bare-bones validation for a method
	 * 
	 * @param method method to be validated
	 */
	public void validateCommandMethod(Method method, CustomCommandAPI methodObject) throws InvalidMethodException {
		//todo remove once implemented parameter flagging and other parameter forwarding
		for (Type parameter : method.getParameterTypes())
			if (parameter != CommandSender.class && parameter != Command.class && parameter != String.class && parameter != String[].class)
				throw new InvalidMethodException("Invalid Parameter Types for method " + method + ". Only accepted (CommandSender, Command, String, String[])");

		if (methodObject.getMethodMap().get("@commandAnnotation@" + this.getId()) != method)
			throw new InvalidMethodException("Method is not contained within the supplied methodObject > " + method);

		this.commandMethod = method;
		this.commandMethodContainer = methodObject;
	}

	/**
	 * Temporary bare-bones validation for a method
	 *
	 * @param method method to be validated
	 */
	public void validateTabMethod(Method method, CustomCommandAPI methodObject) throws InvalidMethodException {
		//todo remove once implemented parameter flagging and other parameter forwarding
		for (Type parameter : method.getParameterTypes())
			if (parameter != CommandSender.class && parameter != Command.class && parameter != String.class && parameter != String[].class)
				throw new InvalidMethodException("Invalid Parameter Types for method " + method + ". Only accepted (CommandSender, Command, String, String[])");

		if (methodObject.getMethodMap().get("@tabAnnotation@" + this.getId()) != method)
			throw new InvalidMethodException("Method is not contained within the supplied methodObject > " + method);

		if (method.getReturnType() != List.class) //todo create reflection library that can handle edge cases like if it was a subclass
			throw new InvalidMethodException("Method does not have the correct return type. Required List<String>");

		this.tabMethod = method;
		this.tabMethodContainer = methodObject;
	}

	/**
	 * Remove the first n elements of an array
	 *
	 * @param array original array
	 * @return an array[N-n] with the first n element removed from the original
	 */
	private String[] removeFirst(String[] array, int n) {
		//helper method
		String[] newArray = new String[array.length-n];
		System.arraycopy(array, n, newArray, 0, array.length - n);
		return newArray;
	}
	
	/**
	 * Check if a command sender has permission to use the command
	 * 
	 * @param sender command sender that used the command
	 * @return whether command sender has permission to use the command
	 */
	public boolean hasPermissions(CommandSender sender) {
		if (this.ignorePermissions) return true;

		for (String reqPerm : this.requiredPermissions)
			if (!sender.isPermissionSet(reqPerm)) return false;

		for (String perm : this.permissions)
			if (sender.isPermissionSet(perm)) return true;

		return false;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
		baseCommand.setDescription(description);
	}

	public String getUsage() {
		return this.usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
		this.baseCommand.setUsage(usage);
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void clearAliases() {
		this.aliases.clear();
		this.baseCommand.setAliases(new ArrayList<>());
	}

	public void addAlias(String alias) {
		this.aliases.add(alias);
		this.baseCommand.setAliases(this.aliases);
	}

	public void removeAlias(String alias) {
		for (int i = 0; i < aliases.size(); i++)
			if (aliases.get(i).equals(alias)) aliases.remove(i--);
		this.baseCommand.setAliases(aliases);
	}

	/**
	 * Assigns the permission to the appropriate container
	 *
	 * @param permission name of the permission to be added
	 * @param isRequired whether permission is required for the command to execute
	 */
	public void addPermission(String permission, boolean isRequired) {
		if (isRequired) addRequiredPermission(permission);
		else addPermission(permission);
	}

	public Set<String> getPermissions() {
		return this.permissions;
	}

	public void clearPermissions() {
		for (String permission : permissions)
			removePermission(permission);
	}

	public void addPermission(String permission) {
		this.permissions.add(permission);
	}

	public void removePermission(String permission) {
		this.permissions.remove(permission);
	}

	public Set<String> getRequiredPermissions() {
		return this.requiredPermissions;
	}

	public void clearRequiredPermissions() {
		for (String requiredPermission : requiredPermissions)
			removeRequiredPermission(requiredPermission);
	}

	public void addRequiredPermission(String requiredPermission) {
		this.requiredPermissions.add(requiredPermission);
	}

	public void removeRequiredPermission(String requiredPermissions) {
		this.requiredPermissions.remove(requiredPermissions);
	}

	public CustomCommand getParent() {
		return this.parent;
	}

	/**
	 * Sets parent command and updates links appropriately
	 *
	 * @param parent parent command
	 */
	public void setParent(CustomCommand parent) {
		if (this.parent != null) this.parent.removeChildCommand(this);
		this.parent = parent;
		this.parent.children.add(this);
	}

	/**
	 * Whether this command is the base parent command
	 *
	 * @return whether this command is the base parent command
	 */
	public boolean isParent() {
		return this.parent == null;
	}

	public Set<CustomCommand> getChildren() {
		return this.children;
	}

	public void addChildCommand(CustomCommand child) {
		this.children.add(child);
		child.parent = this;
	}

	public CustomCommand getChildrenByName(String childName) {
		for (CustomCommand cmd : this.children)
			if (cmd.getName().equals(childName) || cmd.getAliases().contains(childName)) return cmd;
		return null;
	}

	public void removeChildCommand(CustomCommand child) {
		this.children.remove(child);
		child.parent = null;
	}

	public Method getCommandMethod() {
		return this.commandMethod;
	}

	public Method getTabMethod() {
		return this.tabMethod;
	}

	public CustomCommandAPI getCommandMethodContainer() {
		return this.commandMethodContainer;
	}

	public CustomCommandAPI getTabMethodContainer() {
		return this.tabMethodContainer;
	}

	public BaseCommand getBaseCommand() {
		return this.baseCommand;
	}

	/**
	 * Kills the previous baseCommand as a safety check and sets a new one
	 *
	 * @param baseCommand new baseCommand
	 */
	public void setBaseCommand(BaseCommand baseCommand) {
		//safety check
		if (this.baseCommand != null) this.baseCommand.kill();
		if (baseCommand.getCommandExecutor() != null) baseCommand.getCommandExecutor().baseCommand = null;

		this.baseCommand = baseCommand;
		this.baseCommand.setExecutor(this);
	}

	public CustomCommandManager getManager() {
		return this.manager;
	}

	public boolean doesShowAliasesInTabCompletion() {
		return this.showAliasesInTabCompletion;
	}

	public void hideAliasesInTabCompletion() {
		this.showAliasesInTabCompletion = false;
	}

	public void showAliasesInTabCompletion() {
		this.showAliasesInTabCompletion = true;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void hide() {
		this.visible = false;
	}

	public void show() {
		this.visible = true;
	}

	public boolean isDisabled() {
		return this.disabled;
	}

	public void disable() {
		this.disabled = true;
	}

	public void unDisable() {
		this.disabled = false;
	}

	public boolean doesIgnorePermissions() {
		return this.ignorePermissions;
	}

	public void ignorePermissions() {
		this.ignorePermissions = true;
	}

	public void checkPermissions() {
		this.ignorePermissions = false;
	}

	public boolean hasMethod() {
		return this.commandMethod != null;
	}

	/**
	 * Stringify the command with the name and id (full path)
	 *
	 * @return String representation of the command
	 */
	@Override
	public String toString() {
		StringBuilder children = new StringBuilder("{");
		for (CustomCommand customCommand : this.children)
			children.append(customCommand).append(",");
		children.deleteCharAt(children.length()-1).append("}");

		return this.getName() + "@" + this.id + " " + children;
	}
	
}
