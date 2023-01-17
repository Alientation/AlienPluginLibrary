package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

/**
 * Util to compute matches
 * <p>
 *
 * Match Condition
 * <p>
 *
 * MatchType
 */
public class CommandArgumentMatch {

    /**
     * Checks the argument against a match condition
     * <p>
     *
     *
     * @param arg argument to be checked
     * @param matchCondition Condition to be checked with
     * @return whether it matches
     */
    public static boolean doesMatchCondition(String arg, String matchCondition) {
        return true;
    }

    /**
     * Checks the argument against a match type
     *
     * @param arg argument to be checked
     * @param matchType Condition to be checked with
     * @return whether it matches
     */
    public static boolean doesMatchType(String arg, String matchType) {
        return true;
    }

    /**
     * Type identifiers
     */
    enum Type {
        INTEGER("int"), STRING("str"), CHARACTER("ch"), FLOAT("f");

        final String type;
        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    /**
     * Condition identifiers
     */
    enum Condition {
        TEMP("");

        final String condition;
        Condition(String condition) {
            this.condition = condition;
        }

        public String getCondition() {
            return this.condition;
        }

        @Override
        public String toString() {
            return this.condition;
        }
    }
}
