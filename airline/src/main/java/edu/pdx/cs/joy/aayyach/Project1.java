package edu.pdx.cs.joy.aayyach;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.google.common.annotations.VisibleForTesting;

/**
 * The main class for the Airline Project.
 */
public class Project1 {

  /**
   * This method parses the dateAndTime string to check if it is valid. In the case that it is not valid, a DateTimeParseException is raised. 
   * 
   * @param dateAndTime the date and time strings
   * @return  boolean value (true) if the date and time are valid
   */
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    try {
      LocalDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("M/d/yyyy H:mm")); 
      return true;
    }
    catch (DateTimeParseException e) {
      System.err.println("Invalid date entered for departing or arriving flight. Please try again."); 
      return false;
    }
  }

  /**
   * This method prints a helpful description of how to use the program in the case that a user runs the program with no arguments.
   */
  public static void printHelpfulDesc() {
    System.out.println("usage: java -jar target/airline-1.0.0.jar [options] <args>");
    System.out.println("args are (in this order):\nairline\t\t\tThe name of the airline\nflightNumber\t\tThe flight number\nsrc\t\t\tThree-letter code of departure airport");
    System.out.println("depart\t\t\tDeparture date and time (24-hour time)\ndest\t\t\tThree-letter code of arrival airport\narrive\t\t\tArrival date and time (24-hour time).");
    System.out.println("options are (options may appear in any order):\n-print\t\t\tPrints a description of the new flight\n-README\t\t\tPrints a README for this project and exits");
    System.out.println("Date and time should be in the format: mm/dd/yyyy hh:mm");
  }

  /**
   * This method prints the README.txt file.
   */
  public static void printREADMEFile() {
    try(InputStream readme = Project1.class.getResourceAsStream("README.txt")) {
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

  /**
   * This is the main method of Project1 that parses the command line and allows us to run the program.
   * 
   * @param args  The command line arguments
   */
  public static void main(String[] args) {

    // Prints helpful information on how to run the program if no arguments are provided.
    if (args.length == 0) { 
      printHelpfulDesc(); 
      return;
    }
    // Checks if there is not enough command line args
    else if (args.length < 8 || (args.length == 8) && (args[0].startsWith("-") || args[1].startsWith("-"))) {
      System.err.println("Error: Not enough command line arguments\nThis program has 8 required args that must be entered when running the program. Refer below to see how to run the program correctly.");
      printHelpfulDesc();
      return;
    }
    // Checks if there are more command line args than required
    else if ((args.length >= 9 && (!args[0].startsWith("-"))) || ((args.length == 10) && (!args[0].startsWith("-") || !args[1].startsWith("-")))) {
      System.err.println("Error: Too many command line arguments\nThis program has 8 required args and 2 optional options that must be entered when running the program. Refer below to see how to run the program correctly.");
      printHelpfulDesc();
      return;
    }
    // Case for the required command line args
    else if (args.length == 8) {
      try {
        Airline airline = new Airline(args[0]);
        isValidDateAndTime(args[3] + " " + args[4]);
        isValidDateAndTime(args[6] + " " + args[7]); 
        Flight flight = new Flight(Integer.parseInt(args[1]), args[2], args[3], args[4], args[5], args[6], args[7]);
        airline.addFlight(flight);
      }
      catch (NumberFormatException e) {
        System.err.println("The flight number can only be numerical digits. Please try running the program again with the appropriate input for the flight number.");
      }
      catch (IllegalArgumentException e) {
        System.err.println(e.getMessage());
      }
    }
    // Case for the required command line args and one option
    else if (args.length == 9) {
      try {
        if (args[0].equalsIgnoreCase("-README")) {
          printREADMEFile();
          return;
        }
        else if (!args[0].equalsIgnoreCase("-print")) {
          System.err.println(args[0] + " is not recognized as a valid option.");
          System.err.println("Valid options are -README and -print."); 
          return;
        }

        Airline airline = new Airline(args[1]);
        isValidDateAndTime(args[4] + " " + args[5]);
        isValidDateAndTime(args[7] + " " + args[8]); 
        Flight flight = new Flight(Integer.parseInt(args[2]), args[3], args[4], args[5], args[6], args[7], args[8]);
        airline.addFlight(flight);        
        if (args[0].equalsIgnoreCase("-print")) { System.out.println(flight.toString()); }
      }
      catch (NumberFormatException e) {
        System.err.println("The flight number can only be numerical digits. Please try running the program again with the appropriate input for the flight number.");
      }
      catch (IllegalArgumentException e) {
        System.err.println(e.getMessage());
      }
    }
    // Case for the required command line args along with both options
    else if (args.length == 10) {
      if (args[0].startsWith("-") && args[1].startsWith("-")) {
        try {
          Boolean printOptionExists = false;
          Boolean readMeOptionExists = false;
          if (args[0].equalsIgnoreCase("-README") || args[1].equalsIgnoreCase("-README")) { printREADMEFile(); readMeOptionExists = true; return; }

          Airline airline = new Airline(args[2]);
          isValidDateAndTime(args[5] + " " + args[6]);
          isValidDateAndTime(args[8] + " " + args[9]); 
          Flight flight = new Flight(Integer.parseInt(args[3]), args[4], args[5], args[6], args[7], args[8], args[9]);
          airline.addFlight(flight);

          if (args[0].equalsIgnoreCase("-print") || args[1].equalsIgnoreCase("-print")) { System.out.println(flight.toString()); printOptionExists = true; }
          if (printOptionExists.equals(false) && readMeOptionExists.equals(false)) {
            System.err.println(args[0] + " and" + args[1] + " are not recognized as valid options.");
            System.err.println("Valid options are -README and -print."); 
          }
        }
        catch (NumberFormatException e) {
          System.err.println("The flight number can only be numerical digits. Please try running the program again with the appropriate input for the flight number.");
        }
        catch (IllegalArgumentException e) {
          System.err.println(e.getMessage());
        }
      }
    }
  }
}