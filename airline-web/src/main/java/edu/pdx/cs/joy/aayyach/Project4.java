package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.HashMap;

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
        String src = null; 
        String departDate = null;
        String departTime = null;
        String departExt = null; 
        String dest = null; 
        String arrivalDate = null;
        String arrivalTime = null;
        String arrivalExt = null; 
        int len = args.length;

        boolean readme = false;
        boolean print = false;
        boolean hostExists = false;
        boolean portExists = false;
        boolean search = false; 
        
        HashMap<String, String> options = getOptions(args); 
        if (options != null) {
            for (Map.Entry<String, String> option : options.entrySet()) {
                int i = 0;
                if (option.getKey().equals("-README")) {
                    readme = true;
                    break;
                } else if (option.getKey().equals("-print")) {
                    print = true;
                } else if (option.getKey().equals("-host")) {
                    hostName = option.getValue();
                    hostExists = true;
                } else if (option.getKey().equals("-port")) {
                    portString = option.getValue();
                    portExists = true;
                } else if (option.getKey().equals("-search")) {
                    search = true;
                    airlineName = option.getValue().replace("\"", "");
                    if (i + 1 < args.length) { src = args[i + 1]; }
                    if (i + 2 < args.length) { dest = args[i + 2]; }
                    if (i + 3 < args.length) { error("-search only supports airline name, source, and destination argument."); return; }
                }
                ++i;
            }
        }

        if (readme) {
            printREADMEFile();
            return;
        }
  
        int i = 0;
        for (String arg : args) { 
            if (search) { break; }
            if (args[i].startsWith("-")) { continue; } 
            if (i > 0 && i < args.length && args[i - 1].startsWith("-")) { continue; }
            if (airlineName == null && arg.startsWith("\"") && arg.endsWith("\"")) {
                airlineName = arg.replace("\"", "");
            } else if (flightNumber == null && arg.length() == 3 && arg.matches("\\d{3}")) {
                flightNumber = arg;
            } else if (src == null && arg.length() == 3 && arg.matches("[a-zA-Z]{3}")) {
                src = arg.toUpperCase();
            } else if (departDate == null) {
                departDate = arg;
            } else if (departTime == null) {
                departTime = arg;
            } else if (departExt == null) {
                departExt = arg.toUpperCase();
            } else if (dest == null && arg.length() == 3 && arg.matches("[a-zA-Z]{3}")) {
                dest = arg.toUpperCase();
            } else if (arrivalDate == null) {
                arrivalDate = arg;
            } else if (arrivalTime == null) {
                arrivalTime = arg;
            } else if (arrivalExt == null) {
                arrivalExt = arg.toUpperCase();
            } else {
                usage("Extraneous command line argument: " + arg);
                return;
            }
            ++i;
        }

        if (hostName == null) {
            usage( MISSING_ARGS );
            return;
        }  else if (portString == null) {
            usage( MISSING_ARGS );
            return;
        } else if ( airlineName == null ) {
            error("Missing airline name (ex. \"Airline\")");
            return;
        } else if ( src == null ) {
            error( "Missing src airport" );
            return;
        } else if ( departDate == null ) {
            error( "Missing depart date" );
            return;
        } else if ( departTime == null ) {
            error( "Missing depart time" );
            return;
        } else if ( departExt == null ) {
            error( "Missing depart extension (AM/PM)" );
            return;
        } else if ( dest == null ) {
            error( "Missing destination airport" );
            return;
        } else if ( arrivalDate == null ) {
            error( "Missing arrival date" );
            return;
        } else if ( arrivalTime == null ) {
            error( "Missing arrival time" );
            return;
        } else if ( arrivalExt == null ) {
            error( "Missing arrival extension (AM/PM)");
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
        String depart = departDate + " " + departTime + " " + departExt;
        String arrive = arrivalDate + " " + arrivalTime + " " + arrivalExt;
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
                client.addFlight(airlineName, flightNumber, src, depart, dest, arrive);
                message = Messages.definedAirlineNameAs(airlineName, flightNumber);
            }

            if (print) {
                Airline returned = client.getAirlineAndFlight(airlineName, src, dest);
                PrettyPrinter pretty = new PrettyPrinter(new OutputStreamWriter(System.out));
                pretty.dump(returned);
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

    /**
    * Prints the README.txt file
    */
    public static void printREADMEFile() {
        try(InputStream readMe = Project4.class.getResourceAsStream("README.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(readMe));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
        System.out.println("Could not properly print README file.");
        }
    }

    public static HashMap<String, String> getOptions(String[] args) {
        HashMap<String, String> options = new HashMap<>();
        int len = args.length;

        // Keep looping until i < length of args and we aren't at the airline name
        for (int i = 0; i < len && !args[i].startsWith("\""); ++i) {
            switch(args[i]) {
                case "-host":
                    if (i + 1 >= len) {
                        error("-host must be followed by the hostname (ex. -host localhost)");
                        return null; 
                    }
                    options.put("-host", args[i + 1]); 
                    break;
                case "-port":
                    if (i + 1 >= len) {
                        error("-port must be followed by the port (ex. -port 8080)");
                        return null;
                    }
                    options.put("-port", args[i + 1]); 
                    break;
                case "-README":
                    options.put("-README", null);
                    break;
                case "-print":
                    options.put("-print", null);
                    break;
                case "-search":
                    if (i + 1 >= len) {
                        error("-search must be followed by the airline name (ex. -search \"Airline\")");
                        return null;
                    } 
                    options.put("-search", args[i + 1]);
                    break;
                default:
                    break;
            }
        }
        return options; 
    }
}
