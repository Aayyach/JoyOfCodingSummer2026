package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project3} main class.
 */
class Project3IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project3.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments prints helpful description message.
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardOut(), containsString("usage: java -jar target/airline-1.0.0.jar [options] <args>"));
  }

  /**
   * Tests that invoking the main method with not enough arguments prints a helpful error message.
   */
  @Test
  void testNotEnoughCommandLineArgs() {
    MainMethodResult result = invokeMain("-print", "Airline", "123", "PDX", "07/15/2026",  "12:00", "OAK");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Not enough command line arguments"));
  }

  /**
   * Tests that invoking the main method with too many arguments prints a helpful error message.
   */
  @Test
  void testTooManyCommandLineArgs() {
    MainMethodResult result = invokeMain("bogus", "123", "Airline", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/1/62026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Too many command line arguments"));
  }

  /**
   * Tests that invoking the main method with only the 8 required arguments doesn't produce any text to STDOUT/STDERR
   */
  @Test
  void testOnlyCommandLineArgsPresentDoesNothing() {
    MainMethodResult result = invokeMain("Airline", "123", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString(""));
  }

  /**
   * Tests that invoking the main method with the -README option prints the README.txt and exits
   */
  @Test
  void testReadMeOptionPrintsAndExits() {
    MainMethodResult result = invokeMain("-README", "Airline", "123", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Developer: "));
  }

  /**
   * Tests that invoking the main method with the -print option prints the information about the flight that was provided in the args
   */
  @Test
  void testPrintOptionPrintsFlightInfo() {
    MainMethodResult result = invokeMain("-print", "Airline", "123", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 123 departs PDX"));
  }

  /**
   * Tests that invoking the main method with the -textFile file option creates a new file
   */
  @Test
  void testTextFileOptionsMakesNewFile() {
    invokeMain("-textFile", "test.txt", "Airline", "123", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    Path path = Paths.get("test.txt"); 
    assertThat(Files.exists(path), equalTo(true));
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      System.err.println("File path doesn't exist");
    }
  }

  /**
   * Tests that invoking the main method with the -textFile file option creates a new file and -print prints the flight info
   */
  @Test
  void testTextFileOptionsMakesNewFileAndPrintOptionPrintsFlightInfo() {
    MainMethodResult result = invokeMain("-textFile", "test1.txt", "-print", "Airline", "123", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    Path path = Paths.get("test1.txt"); 
    assertThat(Files.exists(path), equalTo(true));
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      System.err.println("File path doesn't exist");
    }
    assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 123 departs PDX at"));
  }

  /**
   * Tests that invoking the main method with an invalid option prints a helpful error message
   */
  @Test
  void testBogusOptionPrintsGracefulExitMessage() {
    MainMethodResult result = invokeMain("-bogus", "Airline", "123", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: -bogus is not recognized as a valid option"));
  }

  /**
   * Tests that invoking the main method with all valid options and args prints the README.txt file and exits
   */
  @Test
  void testIfAllOptionsAndCommandLineArgsAreEnteredProgramPrintsReadMeAndExits() {
    MainMethodResult result = invokeMain("-README", "-print", "-textFile test.txt", "Airline", "123", "PDX", "07/15/2026", "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Developer: "));
  }

  /**
   * Tests that invoking the main method with all valid arguments and non-valid options prints a helpful error message
   */
  @Test
  void testBogusOptionsAndAllCommandLineArgsAreEnteredProgramExitsWithError() {
    MainMethodResult result = invokeMain("-bogus", "-bogus", "-bogus text", "Airline", "123", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: -bogus is not recognized as a valid option"));
  }

  /**
   * Tests that invoking the main method with all valid arguments, one valid option, and one invalid option prints a helpful error message
   */
  @Test
  void testOneBogusOptionAndOneCommandLineArgsAreEnteredProgramExitsWithError() {
    MainMethodResult result = invokeMain("-README", "-bogus", "Airline", "123", "PDX", "07/15/2026",  "10:00", "PM", "OAK", "07/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: -bogus is not recognized as a valid option"));
  }

  /**
   * Tests that invoking the main method with an invalid flight number prints a helpful error message
   */
  @Test
  void testWithNonNumericFlightNumberPrintsErrorMessage() {
    MainMethodResult result = invokeMain("-print", "Airline", "AAA", "PDX", "7/15/2026", "10:00", "PM", "OAK", "7/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardError(), containsString("The flight number can only be numerical digits."));
  }

  /**
   * Tests that invalid times returns an error message
   */
  @Test 
  void testWithInvalidDepartureTimePrintsErrorMessage() {
      MainMethodResult result = invokeMain("-print", "Airline", "AAA", "PDX", "7/17/2026", "10:00", "PM", "OAK", "7/16/2026", "12:00", "AM");
      assertThat(result.getTextWrittenToStandardError(), containsString("Arrival time cannot be before departure time."));
  }

  /**
   * Tests that invalid departure airport returns an error message
   */
  @Test
  void testWithInvalidDepartureAirportDisplaysErrMessage() {
    MainMethodResult result = invokeMain("-print", "Airline", "123", "AAA", "7/15/2026", "10:00", "PM", "OAK", "7/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardError(), containsString("The departure airport does not exist"));
  }

    /**
   * Tests that invalid arrival airport returns an error message
   */
  @Test
  void testWithInvalidArrivalAirportDisplaysErrMessage() {
    MainMethodResult result = invokeMain("-print", "Airline", "123", "PDX", "7/15/2026", "10:00", "PM", "AAA", "7/16/2026", "12:00", "AM");
    assertThat(result.getTextWrittenToStandardError(), containsString("The arrival airport does not exist"));
  }
}