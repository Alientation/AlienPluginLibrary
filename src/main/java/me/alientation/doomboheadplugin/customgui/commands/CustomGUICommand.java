package me.alientation.doomboheadplugin.customgui.commands;

import me.alientation.doomboheadplugin.customcommand.CustomCommandAPI;
import me.alientation.doomboheadplugin.customcommand.annotations.commands.CommandAnnotation;
import me.alientation.doomboheadplugin.customcommand.annotations.permissions.PermissionAnnotation;
import me.alientation.doomboheadplugin.customgui.CustomGUIBlueprintManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Test command for CustomGUIBlueprint
 */
public class CustomGUICommand extends CustomCommandAPI {
	
	private final CustomGUIBlueprintManager guiManager;
	
	public CustomGUICommand(CustomGUIBlueprintManager guiManager) {
		this.guiManager = guiManager;
	}

	//todo FIGURE OUT WHY THE DEFAULT ON TAB COMPLETE ISN'T SHOWING THE SHOW SUBCOMMAND???

	@CommandAnnotation(id = "customGUI", name = "customGUI", description = "commands for custom gui")
	@PermissionAnnotation(permission =  "customGUI", required = true)
	public boolean guiCommand(CommandSender sender) {
		return true;
	}
	
	@CommandAnnotation(id = "customGUI.show", name = "show", description = "Shows a test gui")
	@PermissionAnnotation(permission = "customGUI", required = true)
	public boolean showGUICommand(CommandSender sender) {
		if (sender instanceof Player) {
			this.guiManager.getGUI("test.gui").open((Player) sender);
			return true;
		}
		sender.sendMessage("A nonliving entity can not access this command");
		return false;
	}
}
