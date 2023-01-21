package me.alientation.doomboheadplugin;

import me.alientation.doomboheadplugin.customcommand.CustomCommandManager;
import me.alientation.doomboheadplugin.customgui.CustomGUIManager;
import me.alientation.doomboheadplugin.customgui.commands.CustomGUICommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DoomboheadPlugin extends JavaPlugin {
    private final CustomCommandManager manager;

    public DoomboheadPlugin() {
        //initiate manager for this plugin on construction
        this.manager = new CustomCommandManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        //loads all annotated commands (through reflection) from a supplied class
        //this.manager.loadCommand(new TestCustomCommand());


        CustomGUIManager guiManager = new CustomGUIManager(this);
        this.manager.loadCommand(new CustomGUICommand(guiManager));

        //registers all commands to the manager
        this.manager.registerCommand();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
