package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Processes match conditions
 */
public class MatchCondition {
    private final String matchCondition;

    public MatchCondition(String matchCondition) {
        this.matchCondition = matchCondition;

    }

    /**
     * Parses whether the argument matches the condition
     * <p>
     *
     * output message when condition failed - can have multiple output messages
     * =================================================================
     * ?"this is sent to command executor" (use escape character '\')
     * <p>
     * ?@p"this is sent to all players"
     * ?@p:name=Alientation"this is sent to the player named Alientation"
     * ?@p:permission=admin"this is sent to players with admin permission"
     * <p>
     * ?@s"this is sent to the server"
     * <p>
     * <p>
     *
     * =================================================================
     * <p>
     *
     * @param argument argument passed by player initiating the command
     * @return match success
     */
    public boolean doesMatch(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String argument) {







        return false;
    }

    public String getMatch() {
        return this.matchCondition;
    }
}
