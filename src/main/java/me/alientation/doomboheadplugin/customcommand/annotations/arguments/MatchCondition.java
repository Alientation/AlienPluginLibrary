package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

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
     *
     *
     *
     * @param argument argument passed by player initiating the command
     * @return match success
     */
    public boolean doesMatch(String argument) {







        return false;
    }

    public String getMatch() {
        return this.matchCondition;
    }
}
