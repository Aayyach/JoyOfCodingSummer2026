package edu.pdx.cs.joy.aayyach;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import edu.pdx.cs.joy.AirportNames;
import java.time.LocalDateTime;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.google.common.annotations.VisibleForTesting;

import edu.pdx.cs.joy.ParserException;

/**
 * The main class for the Airline Project 
 */
public class Project3 {

  /**
   * Parses the command line and allows user to run the program
   * 
   * @param args  Command line arguments
   */
  public static void main(String[] args) {
    // Returns the extracted command line options
    String [] options = getOptions(args); 
    if (options[0] == null) { 
      return; 
    }
    // Returns the extracted airline and flight details
    String [] info = getAirlineAndFlight(args, options); 
    try {
      LocalDateTime dept = isValidDateAndTime(info[3] + " " + info[4] + " " + info[5]);
      LocalDateTime arr = isValidDateAndTime(info[7] + " " + info[8] + " " + info[9]);
      if (arr.isBefore(dept)) {
        System.err.println("Arrival time cannot be before departure time.");
        return;
      }

      if (AirportNames.getName(info[2]) == null) {
        System.err.println("The departure airport does not exist.");
        return;
      }
      else if (AirportNames.getName(info[6]) == null) {
        System.err.println("The arrival airport does not exist.");
        return;
      }
    } catch (Exception e) {
      System.err.println("The departure and/or arrival date is invalid or airport code is invalid.");
    } 

    boolean readMeOptionExists = false;

    // Checks if -README option exists
    for (int i = 0; i < options.length && options[i] != null; ++i) {
      if (options[i].equals("-README")) {
        readMeOptionExists = true;
      }
    }

    Airline airline = null;
    Flight flight = null;

    for (int h = 0; options[h] != null && h < options.length ; h++) {
      // Prints README file and exits program
      if (readMeOptionExists) {
        printREADMEFile();
        return; 
      }

      // Creates airline and flight objects and prints airline and flight info
      if (options[h].equals("-print")) {
        try {
          airline = new Airline(info[0]);
          LocalDateTime departure = isValidDateAndTime(info[3] + " " + info[4] + " " + info[5]);
          LocalDateTime arrival = isValidDateAndTime(info[7] + " " + info[8] + " " + info[9]);
          if (departure != null && arrival != null) {
            flight = new Flight(Integer.parseInt(info[1]), info[2], departure, info[6], arrival);
            System.out.println(flight.toString());
          }
        } catch (NumberFormatException e) {
          System.err.println("The flight number can only be numerical digits. Please try running the program again with the appropriate input for the flight number.");
        }
      }

      // Text file logic
      if (options[h].equals("-textFile")) {
        TextDumper dumper;
        TextParser parser; 
        Path path = Paths.get(options[h + 1]);
        Path parentPath = path.getParent();

        // Case 1: File exists and need to append to file
        if (Files.exists(path)) {
          try {
            parser = new TextParser(Files.newBufferedReader(path)); 
            airline = parser.parse(); 
            dumper = new TextDumper(Files.newBufferedWriter(path));
            LocalDateTime departure = isValidDateAndTime(info[3] + " " + info[4]+ " " + info[5]);
            LocalDateTime arrival = isValidDateAndTime(info[7] + " " + info[8] + " " + info[9]);
            if (!airline.getName().equals(info[0])) {
              System.err.println("Given airline name doesn't match the airline name in the existing file: " + path);
              dumper.dump(airline);
              return;
            }
            else {
              flight = new Flight(Integer.parseInt(info[1]), info[2], departure, info[6], arrival);
              airline.addFlight(flight);
              dumper.dump(airline);
            }
          } catch (ParserException e) {
            System.err.println(e.getMessage());
            return;
          } catch (NullPointerException e) {
            System.err.print("Unable to write to file at this time");
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
        } 
        // Case 2: File doesn't exist
        else {
          try {
            if (parentPath != null) { Files.createDirectories(parentPath); }
            airline = new Airline(info[0]);
            LocalDateTime departure = isValidDateAndTime(info[3] + " " + info[4]+ " " + info[5]);
            LocalDateTime arrival = isValidDateAndTime(info[7] + " " + info[8] + " " + info[9]);
            flight = new Flight(Integer.parseInt(info[1]), info[2], departure, info[6], arrival);
            airline.addFlight(flight);
            dumper = new TextDumper(Files.newBufferedWriter(path));
            dumper.dump(airline);
          } catch (NullPointerException e) {
            System.err.println("Unable to write to this file due to invalid path");
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
        }
      }

      if (options[h].equals("-pretty")) {
        PrettyPrinter dumper;
        Path path;

        // Initialize the path
        if (options[h + 1].equals("-")) { path = null; }
        else { path = Paths.get(options[h + 1]); }

        try {
          // Initalize the pretty printer
          if (path == null) { dumper = new PrettyPrinter(new OutputStreamWriter(System.out)); }
          else { dumper = new PrettyPrinter(Files.newBufferedWriter(path)); }

          airline = new Airline(info[0]);
          LocalDateTime departure = isValidDateAndTime(info[3] + " " + info[4]+ " " + info[5]);
          LocalDateTime arrival = isValidDateAndTime(info[7] + " " + info[8] + " " + info[9]);
          flight = new Flight(Integer.parseInt(info[1]), info[2], departure, info[6], arrival);
          airline.addFlight(flight);
          dumper.dump(airline);
        } catch (Exception e) {
          System.out.println("Something went wrong...");
        }
      }
    } 
  }

  /**
   * Extracts the airline and flight information from the command line arguments
   * 
   * @param arg   Command line arguments
   * @return  String array with the airline and flight information
   */
  public static String [] getAirlineAndFlight(String[] arg, String[] options) {
    String [] info = new String[10]; 
    if (options == null) { return null; }
    else if (!(options == null) && options[0].equals("Not enough") || options[0].equals("Too many")) { return null; }
    
    int size = arg.length;
    if (arg.length == 10) {
      for (int i = 0; i < size; ++i) { 
        info[i] = arg[i]; 
      }
      return info;
    }
 
    // Index variables
    int i = 0;  // for arg index
    int j = 0;  // for info index
    while (i < arg.length) {
      if (arg[i].equals("-textFile")) {
        // Skip over filename
        i += 2;
        continue;
      }
      if (arg[i].equals("-pretty")) {
        // Skip over filename
        i += 2;
        continue;
      }
      if (arg[i].equals("-print") || arg[i].equals("-README")) {
        ++i;
        continue;
      }
      if (j < info.length) {
        info[j] = arg[i]; 
        ++j;
      }
      ++i;
    }
    return info;
  }
   
  /**
   * Extracts the options from the command line arguments
   * 
   * @param arg   Command line arguments
   * @return  String array with the command line arguments
   */
  public static String[] getOptions(String[] arg) {
    String [] args = new String[6]; 
    int len = arg.length;
    // No command line arguments 
    if (len == 0) {
      printHelpfulDesc(); 
      return args; 
    }
    // Not enough command line arguments
    else if (len < 10) {
      args[0] = "Not enough";
      System.err.println("Error: Not enough command line arguments\nThis program has 10 required args that must be entered when running the program. Refer below to see how to run the program correctly.");
      printHelpfulDesc();
      return args;
    }
    // Too many command line arguments
    else if (len > 16) {
      args[0] = "Too many";
      System.err.println("Error: Too many command line arguments\nThis program has 10 required args and 4 optional options that must be entered when running the program. Refer below to see how to run the program correctly.");
      printHelpfulDesc();
      return args;
    }
    // Get the command line arguments
    else if (arg[0].startsWith("-") || arg[1].startsWith("-") || arg[2].startsWith("-") || arg[3].startsWith("-") || arg[4].startsWith("-") || arg[5].startsWith("-")){
      int size = len;
      int index = 0;
      while (index < size) {
        // Skips the filenames
        if (!arg[index].startsWith("-")) {
          ++index;
          continue;
        }

        switch (arg[index]) {
          case "-README":
            args[index] = arg[index]; 
            ++index;
            continue;
          case "-print":
            args[index] = arg[index];
            ++index;
            continue;
          case "-textFile":
            if (index + 1 >= arg.length) {
              System.err.println("Error: -textFile option missing filename. Please try again.");
              args[0] = null;
              return args; 
            }
            args[index] = arg[index]; 
            args[index + 1] = arg[index + 1]; 
            index += 2;
            continue;
          case "-pretty":
              if (index + 1 >= arg.length) {
              System.err.println("Error: -pretty option missing filename. Please try again.");
              args[0] = null;
              return args; 
            }
            args[index] = arg[index]; 
            args[index + 1] = arg[index + 1]; 
            index += 2;
            continue;
          default:
            System.err.println("Error: " + arg[index] + " is not recognized as a valid option");
            System.err.println("Valid options are -README, -print, and -textFile file"); 
            return args;
        }
      }
    }
    // Checks if there are no options in the command line
    else if (len > 10) {
      // Checks if any arguments start with "-"
      boolean noOptions = true;
      for (String elem : arg) {
        if (elem.startsWith("-")) { noOptions = false; break; }
      }
      // If they don't, issue an error message to stderr
      if (noOptions) {
        System.err.println("Error: Too many command line arguments\nThis program has 8 required args and 3 optional options that must be entered when running the program. Refer below to see how to run the program correctly.");
        printHelpfulDesc();
        args[0] = null;
      }
    } 
    return args;
  }

  /**
   * Parses the dateAndTime string to check if it is valid
   * If it is not valid, a DateTimeParseException is raised
   * 
   * @param dateAndTime   the date and time strings
   * @return  boolean value to confirm date and time are valid or not
   */
  @VisibleForTesting
  static LocalDateTime isValidDateAndTime(String dateAndTime) {
    try {
      return LocalDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("M/d/yyyy h:mm a")); 
    }
    catch (DateTimeParseException e) {
      System.err.println("Invalid date entered for departing or arriving flight."); 
      return null;
    }
  }

  /**
   * Prints a helpful description of how to use the program if the user runs the program with no arguments
   */
  public static void printHelpfulDesc() {
    System.out.println("usage: java -jar target/airline-1.0.0.jar [options] <args>");
    System.out.println("args are (in this order):\nairline\t\t\tThe name of the airline\nflightNumber\t\tThe flight number\nsrc\t\t\tThree-letter code of departure airport");
    System.out.println("depart\t\t\tDeparture date and time (24-hour time)\ndest\t\t\tThree-letter code of arrival airport\narrive\t\t\tArrival date and time (24-hour time).");
    System.out.println("options are (options may appear in any order):\n-pretty file\t\t\tPretty print the airline's flights to a text file or stand out (file -)\n-textFile file"
                        + "\t\t\tWhere to read/write the airline info\n-print\t\t\tPrints a description of the new flight\n-README\t\t\tPrints a README for this project and exits");
    System.out.println("Date and time should be in the format: mm/dd/yyyy hh:mm");
  }

  /**
   * Prints the README.txt file
   */
  public static void printREADMEFile() {
    try(InputStream readme = Project3.class.getResourceAsStream("README.txt")) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
        String line = reader.readLine();
      while (line != null) {
        System.out.println(line);
        line = reader.readLine();
      }
    } catch (IOException e) {
      System.out.println("Could not properly print README file.");
    }
  }

}
