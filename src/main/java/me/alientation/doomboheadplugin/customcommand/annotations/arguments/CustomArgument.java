package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import me.alientation.doomboheadplugin.customcommand.exceptions.CommandArgumentException;
import me.alientation.doomboheadplugin.customcommand.exceptions.InvalidMethodException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomArgument extends Argument {
    private final Class<?> matchConditionClass;
    private final Method matchConditionMethod;

    public CustomArgument(String name, String description, String usage,
                          Class<?> matchTypeClass, Method matchTypeMethod, Class<?> matchConditionClass, Method matchConditionMethod,
                          boolean isOptional) {
        super(name, description, usage, null, isOptional, false, false, false);

        this.matchConditionClass = matchConditionClass;
        this.matchConditionMethod = matchConditionMethod;

        validateMethods();
    }

    public void validateMethods() { //todo allow for custom parameter feeding in (probably best to make use of the command parameter matching
        if (matchConditionMethod.getParameterCount() != 1 || matchConditionMethod.getParameterTypes()[0] != String.class)
            throw new InvalidMethodException("Command Argument MatchCondition method does not have the correct parameters. There should be only 1 parameter (String)");
    }

    /**
     * Invokes the supplied match condition checking method and todo forwards requested parameters
     *
     * @param sender the initiator of the command
     * @param command the command that is called
     * @param label the name of the command that is called
     * @param argument argument a player passes to the command
     * @return whether the passed argument matches the conditions
     */
    @Override
    public boolean doesMatchCondition(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String argument) { //todo
        try {
            matchConditionMethod.invoke(sender,command,label,argument);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CommandArgumentException();
        }
        return true;
    }

    public Class<?> getMatchConditionClass() {
        return this.matchConditionClass;
    }

    public Method getMatchConditionMethodMethod() {
        return this.matchConditionMethod;
    }
}
