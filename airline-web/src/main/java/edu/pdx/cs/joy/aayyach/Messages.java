package edu.pdx.cs.joy.aayyach;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    /**
     * Message for missing parameters
     */
    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    /**
     * Message for when an airline is created
     */
    public static String definedAirlineNameAs(String word, String definition)
    {
        return String.format( "Defined %s as %s", word, definition );
    }

    /**
     * Message for deleting all airlines
     */
    public static String allAirlinesDeleted() {
        return "All dictionary entries have been deleted";
    }

}
