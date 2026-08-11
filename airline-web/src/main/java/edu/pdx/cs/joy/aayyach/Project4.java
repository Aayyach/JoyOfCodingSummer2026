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
import java.util.ArrayList;

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
        ArrayList<String> commandArgs = getArgs(args);

        if (commandArgs == null || commandArgs.isEmpty()) {
            usage(MISSING_ARGS);
            return;
        } 

        if (options == null) {
            return; 
        } else {
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
                    String [] parts = option.getValue().split(","); 
                    int argLen = parts.length; 

                    // Only airlineName is supplied
                    if (i + 1 == argLen) { airlineName = parts[0].trim(); }
                    // airlineName + source is supplied
                    else if (i + 2 == argLen) { airlineName = parts[0].trim(); src = parts[1].trim(); }
                    // airlineName + source + dest is supplied
                    else if (i + 3 == argLen) { airlineName = parts[0].trim(); src = parts[1].trim(); dest = parts[2].trim(); }
                }
                ++i;
            }
        }

        if (readme) {
            printREADMEFile();
            return;
        }
        
        if (!search) {
            // Gets airline name
            if (airlineName == null) {
                airlineName = commandArgs.get(0);
            }
            // Gets flight number
            if (flightNumber == null && commandArgs.get(1).length() == 3 && commandArgs.get(1).matches("\\d{3}") && !search) {
                flightNumber = commandArgs.get(1);
            } else {
                error("Flight number must be 3 numerical characters"); 
                return; 
            }
            // Gets source airport
            if (src == null && commandArgs.get(2).length() == 3 && commandArgs.get(2).matches("[a-zA-Z]{3}")) {
                src = commandArgs.get(2).toUpperCase();
            } else {
                error("Source airport must be 3 alphabetical characters");
                return;
            }
            // Gets departure date 
            if (departDate == null) {
                departDate = commandArgs.get(3);
            } 
            // Gets departure time
            if (departTime == null) {
                    departTime = commandArgs.get(4);
            }
            // Gets departure extension (AM/PM)
            if (departExt == null) {
                departExt = commandArgs.get(5).toUpperCase();
            } 
            // Get the destination airport
            if (dest == null && commandArgs.get(6).length() == 3 && commandArgs.get(6).matches("[a-zA-Z]{3}")) {
                    dest = commandArgs.get(6).toUpperCase();
            } else {
                error("Destination airport must be 3 alphabetical characters");
                return;
            }
            // Gets the arrival date
            if (arrivalDate == null) {
                arrivalDate = commandArgs.get(7); 
            } 
            // Gets the arrival time
            if (arrivalTime == null) {
                    arrivalTime = commandArgs.get(8); 
            }
            // Gets arrival extension (AM/PM)
            if (arrivalExt == null) {
                    arrivalExt = commandArgs.get(9).toUpperCase();
            } 
            // Checks for extra args 
            if (commandArgs.size() > 19) {
                usage("Extraneous command line arguments" );
                return;
            }
        } 
        
        if ( hostName == null ) {
            usage( MISSING_ARGS );
            return;
        }  else if ( portString == null ) {
            usage( MISSING_ARGS );
            return;
        } else if ( airlineName == null ) {
            error("Missing airline name (ex. \"Airline\")");
            return;
        } 
        
        if (!search) {
            if ( src == null ) {
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
        } 

        int port;
        try {
            port = Integer.parseInt( portString );
        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AirlineRestClient client = new AirlineRestClient(hostName, port);

        Airline returned = null;
        String message;
        String depart = departDate + " " + departTime + " " + departExt;
        String arrive = arrivalDate + " " + arrivalTime + " " + arrivalExt;
        try {
            if (!search) {
                client.addFlight(airlineName, flightNumber, src, depart, dest, arrive);
                message = Messages.definedAirlineNameAs(airlineName, flightNumber);
            } else {
                if (src == null) { src = "NONE"; }
                if (dest == null) { dest = "NONE"; }
                try {
                    returned = client.searchForAirlines(airlineName, src, dest);
                } catch (RuntimeException e) {
                    System.out.println("There was no airline found with the name: " + airlineName);
                    return;
                }
                PrettyPrinter pretty = new PrettyPrinter(new OutputStreamWriter(System.out));
                pretty.dump(returned);
                message = Messages.definedAirlineNameAs(airlineName, flightNumber);
            }

            if (print) {
                returned = client.getAirlineAndFlight(airlineName, src, dest);
                PrettyPrinter pretty = new PrettyPrinter(new OutputStreamWriter(System.out));
                pretty.dump(returned);
            }
        } catch (IOException | ParserException ex ) {
            error("While contacting server: " + ex.getMessage());
            return;
        }

        if (message.equals(airlineName + " with 0 flights")) {
            System.out.println(airlineName + " airline does not have any flights from " + src + "to " + dest);
            return;
        } else {
            System.out.println(message);
        }
    }

    /**
     * Prints out an error message to stderr
     */
    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
    }

    /**
     * Prints usage information for this program and exits
     * 
     * @param message An error message to print
     */
    private static void usage(String message)
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

    /**
     * Gets the arguments from the command line arguments
     * 
     * @param args the command line argument array
     * @return array containing the arguments
     */
    public static ArrayList<String> getArgs(String [] args) {
        HashMap<String, String> options = getOptions(args);
        if (options == null) { return null; }
        ArrayList<String> argArray = new ArrayList<String>();
        int len = args.length;

        for (int i = 0; i < len; ++i) {
            if (args[i].startsWith("-")) {
                if (args[i].equals("-README") || args[i].equals("-print")) { continue; }
                if (args[i].equals("-host") || args[i].equals("-port")) { ++i; continue; }
                if (args[i].equals("-search")) {
                    if (options.containsKey("-search")) {
                        String temp = options.get("-search");
                        String [] temp2 = temp.split(",");
                        int j = temp2.length;
                        i += j;
                    }
                }
            } 
            argArray.add(args[i]); 
        }
        return argArray; 
    }

    /**
     * Gets the options from the command line arguments
     * 
     * @param args the command line argument array
     * @return hashmap containing the options
     */
    public static HashMap<String, String> getOptions(String [] args) {
        HashMap<String, String> options = new HashMap<>();
        int len = args.length;

        // Keep looping until i < length of args and we aren't at the airline name
        for (int i = 0; i < len; ++i) {
            switch(args[i]) {
                case "-host":
                    // Checks if -host is followed by the hostname
                    if (i + 1 >= len) {
                        error("-host must be followed by the hostname (ex. -host localhost)");
                        return null; 
                    }
                    options.put("-host", args[i + 1]); 
                    ++i;
                    break;
                case "-port":
                    // Checks if -port is followed by the port string
                    if (i + 1 >= len) {
                        error("-port must be followed by the port (ex. -port 8080)");
                        return null;
                    }
                    options.put("-port", args[i + 1]); 
                    ++i;
                    break;
                case "-README":
                    options.put("-README", null);
                    break;
                case "-print":
                    options.put("-print", null);
                    break;
                case "-search":
                    // Checks if the airline name is supplied
                    if (i + 1 >= len) {
                        error("-search must be followed by the airline name (ex. -search \"Airline\")");
                        return null;
                    }
                    // There's a src and dest specified 
                    else if ((i + 3 < len) && !args[i + 2].startsWith("-") && !args[i + 3].startsWith("-")) {
                        if ((args[i + 2].length() != 3) || (!args[i + 2].matches("^[a-zA-Z]+$"))) {
                            error("Source airport must be three letters alphabetical characters in length");
                            return null;
                        }
                        if ((args[i + 3].length() != 3) || (!args[i + 3].matches("^[a-zA-Z]+$"))) {
                            error("Destination airport must be three letters alphabetical characters in length");
                            return null;
                        }
                        options.put("-search", args[i + 1] + "," + args[i + 2] + "," + args[i + 3]);
                        i += 3;
                        break;
                    }
                    // There's a src specified 
                    else if ((i + 2 < len) && !args[i + 2].startsWith("-")) {
                        if ((args[i + 2].length() != 3) || (!args[i + 2].matches("^[a-zA-Z]+$"))) {
                            error("Source airport must be three letters alphabetical characters in length");
                            return null;
                        }
                        options.put("-search", args[i + 1] + "," + args[i + 2]);
                        i += 2;
                        break;
                    }
                    options.put("-search", args[i + 1]);
                    ++i;
                    break;
                default:
                    // Bogus option
                    if (args[i].startsWith("-")) {
                        error("Not recognized as a valid option: " + args[i]);
                        return null; 
                    }  
                    break;
            }
        }
        return options; 
    }
}
