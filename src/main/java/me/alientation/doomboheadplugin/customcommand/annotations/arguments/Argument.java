package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

public class Argument {
    private final String name, description, usage;
    private final String matchType, matchCondition;
    private final boolean isOptional;
    public Argument (String name, String description, String usage, String matchType, String matchCondition, boolean isOptional) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.matchType = matchType;
        this.matchCondition = matchCondition;
        this.isOptional = isOptional;
    }

    public boolean doesMatchType(String argument) {
        return CommandArgumentMatch.doesMatchType(argument,matchType);
    }

    public boolean doesMatchCondition(String argument) {
        return CommandArgumentMatch.doesMatchCondition(argument,matchType);
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

    public boolean isOptional() {
        return this.isOptional;
    }
}
