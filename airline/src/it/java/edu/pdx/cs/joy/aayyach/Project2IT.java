package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project2} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project2.class, args );
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
    MainMethodResult result = invokeMain("-print", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Not enough command line arguments"));
  }

  /**
   * Tests that invoking the main method with too many arguments prints a helpful error message.
   */
  @Test
  void testTooManyCommandLineArgs() {
    MainMethodResult result = invokeMain("bogus", "123", "Airline", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: Too many command line arguments"));
  }

  /**
   * Tests that invoking the main method with only the 8 required arguments doesn't produce any text to STDOUT/STDERR
   */
  @Test
  void testOnlyCommandLineArgsPresentDoesNothing() {
    MainMethodResult result = invokeMain("Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardOut(), containsString(""));
  }

  /**
   * Tests that invoking the main method with the -README option prints the README.txt and exits
   */
  @Test
  void testReadMeOptionPrintsAndExits() {
    MainMethodResult result = invokeMain("-README", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Developer: "));
  }

  /**
   * Tests that invoking the main method with the -print option prints the information about the flight that was provided in the args
   */
  @Test
  void testPrintOptionPrintsFlightInfo() {
    MainMethodResult result = invokeMain("-print", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 123 departs PDX"));
  }

  /**
   * Tests that invoking the main method with the -textFile file option creates a new file
   */
  @Test
  void testTextFileOptionsMakesNewFile() {
    invokeMain("-textFile", "test.txt", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    Path path = Paths.get("test.txt"); 
    assertThat(Files.exists(path), equalTo(true));
  }

  /**
   * Tests that invoking the main method with the -textFile file option creates a new file and -print prints the flight info
   */
  @Test
  void testTextFileOptionsMakesNewFileAndPrintOptionPrintsFlightInfo() {
    MainMethodResult result = invokeMain("-textFile", "test.txt", "-print", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    Path path = Paths.get("test.txt"); 
    assertThat(Files.exists(path), equalTo(true));
    assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 123 departs PDX"));
  }

  /**
   * Tests that invoking the main method with an invalid option prints a helpful error message
   */
  @Test
  void testBogusOptionPrintsGracefulExitMessage() {
    MainMethodResult result = invokeMain("-bogus", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: -bogus is not recognized as a valid option"));
  }

  /**
   * Tests that invoking the main method with all valid options and args prints the README.txt file and exits
   */
  @Test
  void testIfAllOptionsAndCommandLineArgsAreEnteredProgramPrintsReadMeAndExits() {
    MainMethodResult result = invokeMain("-README", "-print", "-textFile test.txt", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardOut(), containsString("Developer: "));
  }

  /**
   * Tests that invoking the main method with all valid arguments and non-valid options prints a helpful error message
   */
  @Test
  void testBogusOptionsAndAllCommandLineArgsAreEnteredProgramExitsWithError() {
    MainMethodResult result = invokeMain("-bogus", "-bogus", "-bogus text", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: -bogus is not recognized as a valid option"));
  }

  /**
   * Tests that invoking the main method with all valid arguments, one valid option, and one invalid option prints a helpful error message
   */
  @Test
  void testOneBogusOptionAndOneCommandLineArgsAreEnteredProgramExitsWithError() {
    MainMethodResult result = invokeMain("-README", "-bogus", "Airline", "123", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("Error: -bogus is not recognized as a valid option"));
  }

  /**
   * Tests that invoking the main method with an invalid flight number prints a helpful error message
   */
  @Test
  void testWithNonNumericFlightNumberPrintsErrorMessage() {
    MainMethodResult result = invokeMain("-print", "Airline", "AAA", "PDX", "07/15/2026",  "20:00", "OAK", "07/15/2026", "22:00");
    assertThat(result.getTextWrittenToStandardError(), containsString("The flight number can only be numerical digits."));
  }
}