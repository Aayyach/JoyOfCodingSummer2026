package edu.pdx.cs.joy.aayyach;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeParseException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.
 */
class Project1Test {

  /**
   * This unit test verifies that the README.txt file can be read as a resource.
   * 
   * @throws IOException
   */
  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
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
    Project1 test = new Project1();
    assertThat(test.isValidDateAndTime("11/11/1111"), equalTo(false));
  }

  /**
   * This unit test checks that the isValidDateAndTime method returns true with a valid date. 
   */
  @Test 
  void isValidDateAndTimeWithValidDateReturnsTrue() {
    Project1 test = new Project1();
    assertThat(test.isValidDateAndTime("7/16/2026 2:00"), equalTo(true));
  }

}
