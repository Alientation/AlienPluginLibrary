package me.alientation.doomboheadplugin.customgui.commands;

import me.alientation.doomboheadplugin.customcommand.CustomCommandAPI;
import me.alientation.doomboheadplugin.customcommand.annotations.commands.CommandAnnotation;
import me.alientation.doomboheadplugin.customcommand.annotations.permissions.PermissionAnnotation;
import me.alientation.doomboheadplugin.customgui.CustomGUIManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Test command for CustomGUI
 */
public class CustomGUICommand extends CustomCommandAPI {
	
	private final CustomGUIManager guiManager;
	
	public CustomGUICommand(CustomGUIManager guiManager) {
		this.guiManager = guiManager;
	}
	
	@CommandAnnotation(id = "customgui.show", name = "show",
			description = "Shows a test gui")
	@PermissionAnnotation(permission = "customgui", required = true)
	public boolean showGUICommand(CommandSender sender) {
		if (sender instanceof Player) {
			this.guiManager.getGUI("test.gui").open((Player) sender);
			return true;
		}
		sender.sendMessage("A nonliving entity can not access this command");
		return false;
	}
}
