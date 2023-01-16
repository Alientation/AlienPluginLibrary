package me.alientation.doomboheadplugin;

import me.alientation.doomboheadplugin.customcommand.CustomCommandManager;
import me.alientation.doomboheadplugin.customcommand.TestCustomCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DoomboheadPlugin extends JavaPlugin {
    private CustomCommandManager manager;

    public DoomboheadPlugin() {
        this.manager = new CustomCommandManager(this); //initiate manager for this plugin on construction
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.manager.loadCommand(new TestCustomCommand()); //loads all annotated commands (through reflection) from a supplied class

        this.manager.registerCommand(); //registers all commands to the manager



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
