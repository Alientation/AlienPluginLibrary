package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import me.alientation.doomboheadplugin.customcommand.exceptions.CommandArgumentException;
import me.alientation.doomboheadplugin.customcommand.exceptions.InvalidMethodException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class representing a custom argument with which a client supplied a method to run the argument checks
 */
public class CustomArgument extends Argument {

    //the class the argument check method is located in
    private final Class<?> matchConditionClass;

    //the method that checks the argument
    private final Method matchConditionMethod;

    /**
     * Constructor for a custom argument
     *
     * @param name name of the argument
     * @param description description of the argument
     * @param usage usage of the argument
     * @param matchConditionClass class the argument check method is located in
     * @param matchConditionMethod the method that checks the argument
     * @param isOptional whether this argument is marked optional (if so there should be no more child commands)
     */
    public CustomArgument(String name, String description, String usage, Class<?> matchConditionClass, Method matchConditionMethod,
                          boolean isOptional) {
        super(name, description, usage, null,
                isOptional, false, false, false,
                -100000000f,100000000f);

        this.matchConditionClass = matchConditionClass;
        this.matchConditionMethod = matchConditionMethod;

        validateMethod();
    }

    /**
     * Validates the supplied method
     */
    public void validateMethod() {
        if (matchConditionMethod.getParameterCount() != 4)
            throw new InvalidMethodException("Invalid Parameter Count for method " + matchConditionMethod + ". Required 4");

        if (matchConditionMethod.getParameterTypes()[0] != CommandSender.class || matchConditionMethod.getParameterTypes()[1] != Command.class || matchConditionMethod.getParameterTypes()[2] != String.class || matchConditionMethod.getParameterTypes()[3] != String.class)
            throw new InvalidMethodException("Invalid Parameter Type for method " + matchConditionMethod + ". Required types (CommandSender, Command, String, String)");
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
