package edu.pdx.cs.joy.aayyach;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A unit test for code in the <code>Project2</code> class.  This is different
 * from <code>Project2IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.
 */
class Project2Test {

  /**
   * This unit test verifies that the README.txt file can be read as a resource.
   * 
   * @throws IOException
   */
  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project2.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("Developer: "));
    }
  }

  /**
   * This unit test checks that the isValidDateAndTime method throws the DateTimeParseException and returns false with an invalid date.
   */
  @Test
  void isValidDateAndTimeWithInvalidDateThrowsExceptionAndReturnsFalse() {
    Project2 test = new Project2();
    assertThat(test.isValidDateAndTime("11/11/1111"), equalTo(false));
  }

  /**
   * This unit test checks that the isValidDateAndTime method returns true with a valid date. 
   */
  @Test 
  void isValidDateAndTimeWithValidDateReturnsTrue() {
    Project2 test = new Project2();
    assertThat(test.isValidDateAndTime("7/16/2026 2:00"), equalTo(true));
  }

  /**
   * This unit test checks that the getOptions method returns all options on the command line
   */
  @Test
  void getOptionsMethodReturnsAllValidArgumentsThatStartWithAHyphen() {
    Project2 test = new Project2();
    String [] elem = {"-README", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9"};
    String [] returned = test.getOptions(elem);
    assertThat(returned[0], equalTo("-README"));
  }

  /**
   * This unit test chekcs that the getOptions method returns null if an invalid option exists in the command line
   */
  @Test
  void getOptionsMethodReturnsNullForInvalidArgumentsThatStartWithAHyphen() {
    Project2 test = new Project2();
    String [] elem = {"-test1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9"};
    String [] returned = test.getOptions(elem);
    assertThat(returned[0], equalTo(null));
  }

  /**
   * This unit test checks that the getOptions method returns "Too many" if there are too many arguments in command line
   */
  @Test
  void getOptionsMethodOutputsTooManyForCommandLineWithTooManyArgs() {
    Project2 test = new Project2();
    String [] elem = {"-README", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9", "t10", "t11", "t12", "t13"};
    String [] returned = test.getOptions(elem);
    assertThat(returned[0], equalTo("Too many"));
  }

  /**
   * This unit test checks that the getOptions method returns "Not enough" if there are not enough args in the command line
   */
  @Test
  void getOptionsMethodOutputsNotEnoughForCommandLineWithTooLittleArgs() {
    Project2 test = new Project2();
    String [] elem = {"-README", "t2", "t3", "t4"};
    String [] returned = test.getOptions(elem);
    assertThat(returned[0], equalTo("Not enough"));
  }

  /**
   * This unit test checks that the getOptions method returns null if there are no options
   */
  @Test
  void getOptionsMethodWithNoOptionsReturnsNull() {
    Project2 test = new Project2();
    String [] elem = {"t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9"};
    String [] returned = test.getOptions(elem);
    assertThat(returned[0], equalTo(null));
  }

  /**
   * This unit test checks that the getAirlineAndFlight method returns null if no options are present
   */
  @Test
  void getAirlineAndFlightWithNullOptionReturnsNull() {
    Project2 test = new Project2();
    String [] args = {"123", "PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00"};
    assertEquals(test.getAirlineAndFlight(args, null), null);
  }

  /**
   * This unit test checks that the getAirlineAndFlight method returns all args if everything on cmd line is valid
   */
  @Test
  void getAirlineAndFlightWithValidOptionsReturnsArgs() {
    Project2 test = new Project2();
    String [] elem = {"-README"};
    String [] args = {"Airline", "123", "PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00"};
    String [] result = test.getAirlineAndFlight(args, elem);
    assertEquals(Arrays.equals(args, result), true);
  }
}
