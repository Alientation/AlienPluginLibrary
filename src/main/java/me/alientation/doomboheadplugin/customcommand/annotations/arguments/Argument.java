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
        for (MatchCondition matchCondition : this.matchConditions)
            if (!matchCondition.doesMatch(argument)) return false; //todo potentially output embedded error message in the condition string
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
