package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Processes match conditions todo finish this
 */
public class MatchCondition {
    //the string representing the match condition
    private final String matchCondition;

    //broken down match condition (split into sub parts)
    private String parsedMatchCondition;

    //the referenced sub parts
    private SubMatchCondition[] subMatchConditions;

    /**
     * Constructs the match condition and breaks the supplied string down into sub parts
     *
     * @param matchCondition supplied match condition to be parsed
     */
    public MatchCondition(String matchCondition) {
        this.matchCondition = matchCondition.replaceAll("\\s+",""); //remove any whitespace characters
        parseMatchCondition();
    }

    /**
     * Breaks the supplied match condition string into sub parts
     */
    public void parseMatchCondition() { //split by parenthesis and boolean operators
        /*
        (()&&()||()) && () || <>

        sub1 && sub2 || sub3

        sub1 -> sub4 && sub5 || sub6

         */




    }

    /**
     * Sub part condition class
     */
    static class SubMatchCondition {
        private final String subMatchCondition;
        private String parsedSubMatchCondition;
        private SubMatchCondition[] subMatchConditions;

        private String outputMessageReceiver;
        private String outputMessage;
        public SubMatchCondition(String subMatchCondition) {
            this.subMatchCondition = subMatchCondition;

        }

    }

    /**
     * Parses whether the argument matches the condition
     * <p></p>
     *
     * <strong>operators</strong> <p>
     * ||  &&  <  >  ==  <=  >= (  )
     * <p></p>
     * <p></p>
     * output message when condition failed - can have multiple output messages
     * =================================================================
     * ?"this is sent to command executor" (use escape character '\')
     * <p></p>
     * ?@p"this is sent to all players"
     * ?@p:name=Alientation"this is sent to the player named Alientation"
     * ?@p:name=[Alientation,Descentation]"this is sent to players named Alientation and Descentation"
     * ?@p:permission=admin"this is sent to players with admin permission"
     * <p></p>
     * ?@s"this is sent to the server"
     * <p></p>
     * <p></p>
     *
     * =================================================================
     * <p></p>
     *
     *
     * @param argument argument passed by player initiating the command
     * @return match success
     */
    public boolean doesMatch(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String argument) {


        return false;
    }



    public String getMatch() {
        return this.matchCondition;
    }
}
