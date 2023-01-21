package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import me.alientation.doomboheadplugin.customcommand.exceptions.InvalidMethodException;

import java.lang.reflect.Method;

public class CustomArgument extends Argument {
    private final Class<?> matchConditionClass;
    private final Method matchConditionMethod;

    public CustomArgument(String name, String description, String usage,
                          Class<?> matchTypeClass, Method matchTypeMethod, Class<?> matchConditionClass, Method matchConditionMethod,
                          boolean isOptional) {
        super(name, description, usage, null, isOptional);

        this.matchConditionClass = matchConditionClass;
        this.matchConditionMethod = matchConditionMethod;

        validateMethods();
    }

    public void validateMethods() { //todo allow for custom parameter feeding in (probably best to make use of the command parameter matching
        if (matchConditionMethod.getParameterCount() != 1 || matchConditionMethod.getParameterTypes()[0] != String.class)
            throw new InvalidMethodException("Command Argument MatchCondition method does not have the correct parameters. There should be only 1 parameter (String)");
    }

    @Override
    public boolean doesMatchCondition(String argument) { //todo
        return true;
    }

    public Class<?> getMatchConditionClass() {
        return this.matchConditionClass;
    }

    public Method getMatchConditionMethodMethod() {
        return this.matchConditionMethod;
    }
}
