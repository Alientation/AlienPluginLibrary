package me.alientation.doomboheadplugin.customcommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

/**
 * Links the BukkitCommand to CustomCommand so that whenever that interactions with this command will be forwarded to the corresponding CustomCommand
 */
public class BaseCommand extends BukkitCommand {
	//whether this command is still active
	private boolean exists;

	//the linked custom command that overrides this object's functionality
	private CustomCommand commandExecutor;

	/**
	 * Instantiates command attributes and links to CustomCommand
	 *
	 * @param name name of command
	 * @param description description of command
	 * @param usageMessage correct usage message
	 * @param aliases other aliases for the command
	 * @param commandExecutor the linked CustomCommand
	 */
	protected BaseCommand(String name, String description, String usageMessage, List<String> aliases, CustomCommand commandExecutor) {
		super(name,description,usageMessage,aliases);
		this.commandExecutor = commandExecutor;
		this.exists = true;
	}

	/**
	 * Executes the linked CustomCommand
	 *
	 * @param sender Source object which is executing this command
	 * @param alias The alias of the command used
	 * @param args All arguments passed to the command, split via ' '
	 * @return Whether the command was executed
	 */
	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
		if (this.exists) return this.commandExecutor.onCommand(sender, this, alias, args);
		
		System.out.println("Broken link between BaseCommand and CustomCommand"); //TODO throw error for this
		return false;
	}

	/**
	 * Generates tab complete from the linked CustomCommand
	 *
	 * @param sender Source object which is executing this command
	 * @param alias The alias being used
	 * @param args All arguments passed to the command, split via ' '
	 * @return The possible options for tab complete
	 */
	public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
		if (this.exists) return Objects.requireNonNull(this.commandExecutor.onTabComplete(sender, this, alias, args));
		
		System.out.println("Broken link between BaseCommand and CustomCommand"); //TODO throw error for this
		return new ArrayList<>();
	}

	/**
	 * Sets the command executor
	 *
	 * @param commandExecutor Custom Command link
	 */
	public void setExecutor(CustomCommand commandExecutor) {
		this.commandExecutor = commandExecutor;
	}

	/**
	 * Gets the command executor
	 *
	 * @return Custom Command link
	 */
	public CustomCommand getCommandExecutor() {
		return commandExecutor;
	}

	/**
	 * Deactivates this command link
	 */
	public void kill() {
		this.exists = false;
	}
}