package me.alientation.doomboheadplugin;

import me.alientation.doomboheadplugin.customcommand.CustomCommandManager;
import me.alientation.doomboheadplugin.customcommand.TestCustomCommand;
import me.alientation.doomboheadplugin.customgui.CustomGUIBlueprint;
import me.alientation.doomboheadplugin.customgui.CustomGUIBlueprintManager;
import me.alientation.doomboheadplugin.customgui.commands.CustomGUICommand;
import me.alientation.doomboheadplugin.customgui.listeners.GUIListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class DoomboheadPlugin extends JavaPlugin {
    private final CustomCommandManager manager;

    private final CustomGUIBlueprintManager guiManager;

    public DoomboheadPlugin() {
        //initiate manager for this plugin on construction
        this.manager = new CustomCommandManager(this);
        this.guiManager = new CustomGUIBlueprintManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        //creates a gui
        CustomGUIBlueprint gui = CustomGUIBlueprint.Builder.newInstance()
                .blueprintID("test gui")
                .blueprintTitle("test.gui")
                .blueprintGUIListener(new GUIListener())
                .build();
        this.guiManager.addBlueprint("test.gui", gui);

        //loads all annotated commands (through reflection) from a supplied class
        this.manager.loadCommand(new TestCustomCommand());
        this.manager.loadCommand(new CustomGUICommand(guiManager));

        //registers all commands to the manager
        this.manager.registerCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
