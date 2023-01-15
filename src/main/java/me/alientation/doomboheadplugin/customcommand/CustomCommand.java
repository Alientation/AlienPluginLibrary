package me.alientation.doomboheadplugin.customcommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 */
public class CustomCommand implements CommandExecutor, TabCompleter {
	//A unique identifier for the specific command. For example the command /help list -> help.list
	private final String id;

	//The parent command. For example the command /help list -> help
	private CustomCommand parent;

	//The sub commands of this parent command. All sub commands inherit the same permission requirements as the parent command
	private final Set<CustomCommand> children;

	//permissions for the current command, TODO link it with the CustomPermission
	private final Set<String> permissions;
	private final Set<String> requiredPermissions;

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
	private boolean showAliasesInTabCompletion = true;


	public static class Builder {
		private CustomCommandManager manager;
		private String id;
		private Collection<String> permissions, requiredPermissions;

		private Builder() {
			permissions = new HashSet<>();
			requiredPermissions = new HashSet<>();
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

		public Builder permissions(Collection<String> permissions) {
			this.permissions = permissions;
			return this;
		}

		public Builder requiredPermissions(Collection<String> requiredPermissions) {
			this.requiredPermissions = requiredPermissions;
			return this;
		}

		public void verify() {
			if (id == null) throw new IllegalStateException("id can't be null");
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
		this.permissions = new HashSet<>();
		this.requiredPermissions = new HashSet<>();
		this.children = new HashSet<>();

		this.permissions.addAll(builder.permissions);
		this.requiredPermissions.addAll(builder.requiredPermissions);

		this.manager = builder.manager;
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
		Bukkit.getPluginManager().callEvent(commandCallAttemptEvent); //initiates event

		//command call is cancelled
		if (commandCallAttemptEvent.isCancelled()) return !commandCallAttemptFail(sender,command,label,args);

		//sender doesn't have permissions
		if (!hasPermissions(sender)) return !invalidPermissions(sender,command,label,args);

		CustomCommand child = args.length > 0 ? getChildrenByName(args[0]) : null;
		//processes down the argument pathway if there exists a children command
		if (child != null) return child.onCommand(sender, command, label, removeFirst(args));

		//no linked on command method
		if (this.commandMethod == null) return !invalidCommand(sender,command,label,args);

		//TODO: Add parameter flag annotations so that the user can greater customize the parameters that get accepted

		//Object[] params = {sender,command,label,args};
		//matches parameters to the command method parameters
		Object[] params = new Object[this.commandMethod.getParameterCount()];
		int paramsIndex = 0;
		for (Class<?> c : this.commandMethod.getParameterTypes()) {
			if (c == CommandSender.class)	params[paramsIndex] = sender;
			else if (c == Command.class) 	params[paramsIndex] = command;
			else if (c == String.class) 	params[paramsIndex] = label;
			else if (c == String[].class) 	params[paramsIndex] = args;
			else 							params[paramsIndex] = null;
			paramsIndex++;
		}
		
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
		if (this.tabMethod != null) { //there is a tab complete method for this
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
			try {
				return (List<String>) this.tabMethod.invoke(this.tabMethodContainer,params);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				e.printStackTrace();
			}
		}

		//default tab complete

		if (args.length > 1) { //there are arguments, so processing of tab complete shouldn't happen here
			CustomCommand child = getChildrenByName(args[0]);
			if (child == null) return null; //no child to process tab complete

			return getChildrenByName(args[0]).onTabComplete(sender, command, label, removeFirst(args)); //child processes tab complete
		}

		List<String> possibleCompletions = new ArrayList<>();
		for (CustomCommand commands : this.children) {
			if (commands.hasPermissions(sender) && commands.getCommandName().indexOf(args[0]) == 0)
				possibleCompletions.add(commands.getCommandName());

			if (!showAliasesInTabCompletion) continue; //don't show aliases on tab completion
			for (String s : commands.getAliases())
				if (s.indexOf(args[0]) == 0) possibleCompletions.add(s);
		}
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
	 * 
	 * @return whether the command should print out the usage description
	 */
	public boolean invalidPermissions(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}
	
	/**
	 * Remove the first element of an array
	 * 
	 * @param array original array
	 * @return an array[N-1] with the first element removed from the original
	 */
	private String[] removeFirst(String[] array) {
		String[] newArray = new String[array.length-1];
		System.arraycopy(array, 1, newArray, 0, array.length - 1);
		return newArray;
	}
	
	/**
	 * Called whenever a sender does not have the appropriate permissions to execute a command
	 * 
	 * @param sender issuer of the command
	 * @param command command that was issued
	 * @param label text that was typed
	 * @param args arguments passed to the command
	 * 
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
	 * 
	 * @return whether the command should print out the usage description
	 */
	public boolean commandCallAttemptSuccess(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}
	
	
	/**
	 * Temporary bare-bones validation for a method
	 * 
	 * @param method method to be validated
	 */
	public void validateCommandMethod(Method method, CustomCommandAPI methodObject) throws InvalidMethodException {
		if (method.getParameterCount() != 4)
			throw new InvalidMethodException("Invalid Parameter Count for method " + method.toString() + ". Required 4");

		if (method.getParameterTypes()[0] != CommandSender.class || method.getParameterTypes()[1] != Command.class || method.getParameterTypes()[2] != String.class || method.getParameterTypes()[3] != String[].class)
			throw new InvalidMethodException("Invalid Parameter Type for method " + method.toString() + ". Required types (CommandSender, Command, String, String[])");

		if (methodObject.getMethodMap().get("@commandAnnotation@" + this.getId()) != method)
			throw new InvalidMethodException("Method is not contained within the supplied methodObject > " + method.toString());

		this.commandMethod = method;
		this.commandMethodContainer = methodObject;
	}
	
	
	/**
	 * Temporary bare-bones validation for a method
	 *
	 * @param method method to be validated
	 */
	public void validateTabMethod(Method method, CustomCommandAPI methodObject) throws InvalidMethodException {
		if (method.getParameterCount() != 4)
			throw new InvalidMethodException("Invalid Parameter Count for method " + method.toString() + ". Required 4");

		if (method.getParameterTypes()[0] != CommandSender.class || method.getParameterTypes()[1] != Command.class || method.getParameterTypes()[2] != String.class || method.getParameterTypes()[3] != String[].class)
			throw new InvalidMethodException("Invalid Parameter Type for method " + method.toString() + ". Required types (CommandSender, Command, String, String[])");

		if (methodObject.getMethodMap().get("@tabAnnotation@" + this.getId()) != method)
			throw new InvalidMethodException("Method is not contained within the supplied methodObject > " + method.toString());

		this.tabMethod = method;
		this.tabMethodContainer = methodObject;
	}
	
	
	/**
	 * Check if a command sender has permission to use the command
	 * 
	 * @param sender command sender that used the command
	 * @return whether command sender has permission to use the command
	 */
	public boolean hasPermissions(CommandSender sender) {
		for (String reqPerm : this.requiredPermissions)
			if (!sender.isPermissionSet(reqPerm)) return false;

		for (String perm : this.permissions)
			if (sender.isPermissionSet(perm)) return true;

		return false;
	}

	/**
	 * Add a single permission
	 *
	 * @param permission permission to be added to this command
	 */
	public void addPermission(String permission) {
		this.permissions.add(permission);
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
	
	/**
	 * Add a single required permission to this command
	 *
	 * @param permission required permission to be added
	 */
	public void addRequiredPermission(String permission) {
		this.requiredPermissions.add(permission);
	}
	
	/**
	 * Adds permissions to the command
	 *
	 * @param permissions permissions to be added
	 */
	public void addPermissions(@NotNull Collection<String> permissions) {
		for (String perm : permissions) addPermission(perm);
	}
	
	/**
	 * Adds required permissions to the command
	 *
	 * @param permissions required permissions to be added
	 */
	public void addRequiredPermissions(@NotNull Collection<String> permissions) {
		for (String perm : permissions) addRequiredPermission(perm);
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

	/**
	 * Adds child command
	 *
	 * @param child child to be added
	 */
	public void addChildCommand(CustomCommand child) {
		this.children.add(child);
		child.parent = this;
	}

	/**
	 * Gets child command by name
	 *
	 * @param childName name of child command
	 * @return child command
	 */
	public CustomCommand getChildrenByName(String childName) {
		for (CustomCommand cmd : this.children)
			if (cmd.getCommandName().equals(childName) || cmd.getAliases().contains(childName)) return cmd;
		return null;
	}

	/**
	 * Removes child command
	 *
	 * @param child child command
	 */
	public void removeChildCommand(CustomCommand child) {
		this.children.remove(child);
		child.parent = null;
	}

	/**
	 * Removes child command by name
	 *
	 * @param childName name of child command
	 */
	public void removeChildCommand(String childName) {
		removeChildCommand(getChildrenByName(childName));
	}


	/**
	 * Gets aliases of this command
	 *
	 * @return aliases of this command
	 */
	public List<String> getAliases() {
		return this.baseCommand.getAliases();
	}

	/**
	 * Returns whether tab completion shows aliases
	 *
	 * @return whether tab completion shows aliases
	 */
	public boolean showAliasesInTabCompletion() {
		return showAliasesInTabCompletion;
	}

	/**
	 * Sets whether tab completion shows aliases
	 *
	 * @param showAliasesInTabCompletion whether tab completion shows aliases
	 */
	public void setShowAliasesInTabCompletion(boolean showAliasesInTabCompletion) {
		this.showAliasesInTabCompletion = showAliasesInTabCompletion;
	}

	/**
	 * Gets command ID
	 *
	 * @return command ID (command pathway)
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Gets name of the command
	 *
	 * @return name of the command
	 */
	public String getCommandName() {
		return this.baseCommand.getName();
	}

	/**
	 * Gets the permissions of this command
	 *
	 * @return permission list
	 */
	public Set<String> getPermission() {
		return this.permissions;
	}

	/**
	 * Gets the required permissions of this command
	 *
	 * @return required permission list
	 */
	public Set<String> getRequiredPermission() {
		return this.requiredPermissions;
	}

	/**
	 * Gets parent command
	 *
	 * @return parent command
	 */
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

	/**
	 * Gets children of this command
	 *
	 * @return children commands
	 */
	public Set<CustomCommand> getChildren() {
		return this.children;
	}

	/**
	 * Returns whether there is a linked method to execute on action
	 *
	 * @return whether there is a linked method to execute on action
	 */
	public boolean hasMethod() {
		return this.commandMethod != null;
	}

	/**
	 * Returns the manager of this command
	 *
	 * @return manager of this command
	 */
	public CustomCommandManager getManager() {
		return this.manager;
	}
	
	@Override
	public String toString() {
		return this.getCommandName() + "@" + this.id;
	}
	
}
