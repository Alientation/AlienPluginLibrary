package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import java.lang.reflect.Method;

public class CustomArgument extends Argument {
    private final Class<?> matchTypeClass;
    private final Method matchTypeMethod;

    private final Class<?> matchConditionClass;
    private final Method matchConditionMethod;

    public CustomArgument(String name, String description, String usage,
                          Class<?> matchTypeClass, Method matchTypeMethod, Class<?> matchConditionClass, Method matchConditionMethod) {
        super(name, description, usage, null, null);

        this.matchTypeClass = matchTypeClass;
        this.matchTypeMethod = matchTypeMethod;
        this.matchConditionClass = matchConditionClass;
        this.matchConditionMethod = matchConditionMethod;
    }

    @Override
    public boolean doesMatchType(String argument) { //todo
        return true;
    }

    @Override
    public boolean doesMatchCondition(String argument) { //todo
        return true;
    }

    public Class<?> getMatchTypeClass() {
        return this.matchTypeClass;
    }

    public Method getMatchTypeMethod() {
        return this.matchTypeMethod;
    }

    public Class<?> getMatchConditionClass() {
        return this.matchConditionClass;
    }

    public Method getMatchConditionMethodMethod() {
        return this.matchConditionMethod;
    }
}
