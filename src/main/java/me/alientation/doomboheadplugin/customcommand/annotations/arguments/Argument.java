package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

public class Argument {
    private final String name, description, usage;
    private final String matchType, matchCondition;

    public Argument (String name, String description, String usage, String matchType, String matchCondition) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.matchType = matchType;
        this.matchCondition = matchCondition;
    }

    public boolean doesMatchType(String argument) { //todo
        return true;
    }

    public boolean doesMatchCondition(String argument) { //todo
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

    public String getMatchType() {
        return this.matchType;
    }

    public String getMatchCondition() {
        return this.matchCondition;
    }
}
