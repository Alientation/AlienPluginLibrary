package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Command Argument with argument attributes and match conditions
 */
public class Argument { //todo test the hardcoded conditionals
    //todo display somewhere idk
    private final String name, description, usage;

    //checks arguments against these match conditions
    private final MatchCondition[] matchConditions;

    //whether this command argument is optional
    private final boolean isOptional;

    //hardcoded checks
    private final boolean checkValidPlayerName;
    private final boolean checkValidInteger;
    private final boolean checkValidFloat;

    /**
     * Constructor
     *
     * @param name name of command argument
     * @param description description of command argument
     * @param usage usage message if user does not use the command argument properly
     * @param matchConditions match condition array
     * @param isOptional optional command argument
     */
    public Argument (String name, String description, String usage, MatchCondition[] matchConditions,
                     boolean isOptional, boolean checkValidPlayerName, boolean checkValidInteger, boolean checkValidFloat,
                     float minNumber, float maxNumber) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.matchConditions = matchConditions;
        this.isOptional = isOptional;

        //todo, when match condition is completed, simply just add match conditions to the existing match conditions instead of these hardcoded checks
        this.checkValidPlayerName = checkValidPlayerName;
        this.checkValidInteger = checkValidInteger;
        this.checkValidFloat = checkValidFloat;
    }

    /**
     * Whether the passed argument matches the conditions
     *
     * @param sender the initiator of the command
     * @param command the command that is called
     * @param label the name of the command that is called
     * @param argument argument a player passes to the command
     * @return success of match
     */
    public boolean doesMatchCondition(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String argument) {
        if (checkValidPlayerName) if (!checkValidPlayerName(sender,command,label,argument)) return false;
        if (checkValidInteger) if (!checkValidInteger(sender,command,label,argument)) return false;
        if (checkValidFloat) if (!checkValidFloat(sender,command,label,argument)) return false;

        for (MatchCondition matchCondition : this.matchConditions)
            if (!matchCondition.doesMatch(sender, command, label, argument)) return false; //todo output embedded error message in the condition string
        return true;
    }

    /**
     * Checks if the argument passed is an online player
     *
     * @param sender the initiator of the command
     * @param command the command that is called
     * @param label the name of the command that is called
     * @param argument argument a player passes to the command
     * @return success of match
     */
    public boolean checkValidPlayerName(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String argument) {
        return sender.getServer().getPlayer(argument) != null;
    }

    /**
     * Checks if the argument passed is an integer
     *
     * @param sender the initiator of the command
     * @param command the command that is called
     * @param label the name of the command that is called
     * @param argument argument a player passes to the command
     * @return success of match
     */
    public boolean checkValidInteger(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String argument) {
        try {
            Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the argument passed is an float
     *
     * @param sender the initiator of the command
     * @param command the command that is called
     * @param label the name of the command that is called
     * @param argument argument a player passes to the command
     * @return success of match
     */
    public boolean checkValidFloat(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String argument) {
        try {
            Float.parseFloat(argument);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public MatchCondition[] getMatchCondition() {
        return this.matchConditions;
    }

    public boolean isOptional() {
        return this.isOptional;
    }

    public boolean doesCheckValidPlayerName() {
        return this.checkValidPlayerName;
    }

    public boolean doesCheckValidInteger() {
        return this.checkValidInteger;
    }

    public boolean doesCheckValidFloat() {
        return this.checkValidFloat;
    }

    /**
     * Converts string array to MatchCondition objects
     *
     * @param matchConditions input string array of match conditions
     * @return array of MatchCondition objects
     */
    public static MatchCondition[] extractMatchConditions(String[] matchConditions) {
        MatchCondition[] converted = new MatchCondition[matchConditions.length];
        for (int i = 0; i < matchConditions.length; i++)
            converted[i] = new MatchCondition(matchConditions[i]);
        return converted;
    }
}
