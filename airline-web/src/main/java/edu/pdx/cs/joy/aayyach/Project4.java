package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String... args) {
        String hostName = null;
        String portString = null;
        String airlineName = null;
        String flightNumber = null;

        for (String arg : args) {
            if (hostName == null) {
                hostName = arg;

            } else if ( portString == null) {
                portString = arg;

            } else if (airlineName == null) {
                airlineName = arg;

            } else if (flightNumber == null) {
                flightNumber = arg;

            } else {
                usage("Extraneous command line argument: " + arg);
            }
        }

        if (hostName == null) {
            usage( MISSING_ARGS );
            return;

        } else if ( portString == null) {
            usage( "Missing port" );
            return;
        } else if ( airlineName == null ) {
            usage( "Missing airline name" );
            return;
        }

        int port;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AirlineRestClient client = new AirlineRestClient(hostName, port);

        String message;
        try {
            if (flightNumber == null) {
                // Print all dictionary entries
                Airline airline = client.getAirline(airlineName); 

                StringWriter sw = new StringWriter(); 
                PrettyPrinter pretty = new PrettyPrinter(sw); 
                pretty.dump(airline);
                message = sw.toString(); 

            } else {
                // Post the airlineName/flightNumber pair
                client.addFlight(airlineName, flightNumber);
                message = Messages.definedAirlineNameAs(airlineName, flightNumber);
            }

        } catch (IOException | ParserException ex ) {
            error("While contacting server: " + ex.getMessage());
            return;
        }

        System.out.println(message);
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [airlineName] [flightNumber]");
        err.println("  host         Host of web server");
        err.println("  port         Port of web server");
        err.println("  airlineName         airlineName in dictionary");
        err.println("  flightNumber   flightNumber of airlineName");
        err.println();
        err.println("This simple program posts airlineNames and their flightNumbers");
        err.println("to the server.");
        err.println("If no flightNumber is specified, then the airlineName's flightNumber");
        err.println("is printed.");
        err.println("If no airlineName is specified, all dictionary entries are printed");
        err.println();
    }
}