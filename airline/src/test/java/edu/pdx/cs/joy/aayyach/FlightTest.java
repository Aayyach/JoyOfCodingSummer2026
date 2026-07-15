package edu.pdx.cs.joy.aayyach;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Flight} class.
 */
public class FlightTest {

  /**
   * This unit test checks that the getSource method returns the correct String (source).
   */
  @Test 
  void getSourceStringReturnsCorrectSource() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertEquals(flight.getSource(), "PDX"); 
  }

  /**
   * This unit test checks that the getNumber method returns the correct flight number. 
   */
  @Test
  void getNumberReturnsCorrectNumber() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThat(flight.getNumber(), equalTo(123)); 
  }

  /**
   * This unit test checks that the getDestination method returns the correct String (dest).
   */
  @Test
  void getDestinationReturnsCorrectDestString() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertEquals(flight.getDestination(), "OAK");
  }

  /**
   * This unit test checks that the getDepartureString method returns the correct String (sourceDate + sourceTime).
   */
  @Test 
  void getDepatureStringReturnsCorrectDateAndTimeString() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertEquals(flight.getDepartureString(), "07/14/2026 20:00");
  }

  /**
   * This unit test checks that the getArrivalString method returns the correct String (destDate + destTime).
   */
  @Test
  void getArrivalStringReturnsCorrectDateAndTimeString() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertEquals(flight.getArrivalString(), "07/14/2026 22:00");
  }

  /**
   * This unit test is to validate that the checkSourceOrDest method throws an exception if the input is empty.
   */
  @Test
  void checkSourceorDestWithEmptyInputThrowsException() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThrows(IllegalArgumentException.class, () -> flight.checkSourceOrDest("")); 
  }

  /**
   * This unit test is to validate that the checkSourceOrDest method throws an exception if the input is not alphabetical.
   */
  @Test
  void checkSourceorDestWithNoAlphabeticalCharsThrowsException() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThrows(IllegalArgumentException.class, () -> flight.checkSourceOrDest("123")); 
  }

  /**
   * This unit test is to validate that the checkSourceOrDest method throws an exception if the input is not fully alphabetical.
   */
  @Test
  void checkSourceorDestWithLettersAndNumbersThrowsException() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThrows(IllegalArgumentException.class, () -> flight.checkSourceOrDest("1AA")); 
  }

  /**
   * This unit test is to validate that the checkSourceOrDest method returns true with valid input.
   */
  @Test
  void checkSourceorDestWithValidCharactersReturnsTrue() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertEquals(flight.checkSourceOrDest("PDX"), true); 
  }

  /**
   * This unit test is to validate that the checkDate method throws an exception if the input length is valid, but the format is incorrect.
   */
  @Test
  void checkDateWithInvalidDateFormatThrowsException() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThrows(IllegalArgumentException.class, () -> flight.checkDate("123456789")); 
  }

  /**
   * This unit test is to validate that the checkDate method throws an exception if the input length is valid, but the format is incorrect (alphabetical). 
   */
  @Test
  void checkDateWithInvalidInputThrowsException() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThrows(IllegalArgumentException.class, () -> flight.checkDate("TestTestTest")); 
  }

  /**
   * This unit test is to validate that the checkDate method returns true with a valid date.
   */
  @Test 
  void checkDateWithValidDateReturnsTrue() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertEquals(flight.checkDate("7/14/2026"), true); 
  }

  /**
   * This unit test is to validate that the checkTime method throws an exception with empty input.
   */
  @Test
  void checkTimeWithEmptyInputThrowsException() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThrows(IllegalArgumentException.class, () -> flight.checkTime(" ")); 
  }

  /**
   * This unit test is to valdiate that the checkTime method throws an exception with invalid input (alphabetical).
   */
  @Test
  void checkTimeWithInvalidInputThrowsException() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThrows(IllegalArgumentException.class, () -> flight.checkTime("TestTest")); 
  }

  /**
   * This unit test is to validate that the checkTime method throws an exception with invalid input (digits but not correctly formatted).
   */
  @Test
  void checkTimeWithInvalidTimeFormatThrowsException() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertThrows(IllegalArgumentException.class, () -> flight.checkTime("2000")); 
  }

  /**
   * This unit test is to validate that the checkTime method returns true if the input is valid.
   */
  @Test
  void checkTimeWithValidInputReturnsTrue() {
    Flight flight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    assertEquals(flight.checkTime("20:00"), true); 
  }
  
}
