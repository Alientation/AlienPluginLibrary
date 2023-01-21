package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

/**
 *
 *
 *
 */
public class MatchCondition {
    private final String match;

    public MatchCondition(String match) {
        this.match = match;
    }

    public boolean doesMatch() {
        return false;
    }

    public String getMatch() {
        return this.match;
    }
}
