package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

/**
 * Command Argument with details and match conditions
 */
public class Argument {
    //todo display somewhere idk
    private final String name, description, usage;

    //checks arguments against these match conditions
    private final MatchCondition[] matchConditions;

    //whether this command argument is optional
    private final boolean isOptional;

    /**
     * Constructor
     *
     * @param name name of command argument
     * @param description description of command argument
     * @param usage usage message if user does not use the command argument properly
     * @param matchConditions match condition array
     * @param isOptional optional command argument
     */
    public Argument (String name, String description, String usage, MatchCondition[] matchConditions, boolean isOptional) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.matchConditions = matchConditions;
        this.isOptional = isOptional;
    }

    /**
     * Whether the passed argument matches the conditions
     *
     * @param argument argument a player passes to the command
     * @return success of match
     */
    public boolean doesMatchCondition(String argument) {
        return false;
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
}
