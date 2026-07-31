package edu.pdx.cs.joy.aayyach;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for the {@link Flight} class.
 */
public class FlightTest {

/**
 * This unit test checks to make sure that the default constructor of Flight works as expected.
 */
@Test 
void checkDefaultConstructorOfFlight() {
    Flight flight = new Flight();
    assertEquals(flight.getSource(), "");
    assertEquals(flight.getDeparture(), null);
    assertEquals(flight.getDestination(), "");
    assertEquals(flight.getArrival(), null);
    assertEquals(flight.getNumber(), 0);
}
  /**
   * This unit test checks that the getSource method returns the correct String (source).
   */
  @Test 
  void getSourceStringReturnsCorrectSource() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertEquals(flight.getSource(), "PDX"); 
  }

  /**
   * This unit test checks that the getNumber method returns the correct flight number. 
   */
  @Test
  void getNumberReturnsCorrectNumber() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertThat(flight.getNumber(), equalTo(123)); 
  }

  /**
   * This unit test checks that the getDestination method returns the correct String (dest).
   */
  @Test
  void getDestinationReturnsCorrectDestString() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertEquals(flight.getDestination(), "OAK");
  }

  /**
   * This unit test checks that the getDepartureString method returns the correct String (sourceDate + sourceTime).
   */
  @Test 
  void getDepatureStringReturnsCorrectDateAndTimeString() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertEquals(flight.getDepartureString(), "7/29/26, 8:00 AM");
  }

  /**
   * This unit test checks that the getArrivalString method returns the correct String (destDate + destTime).
   */
  @Test
  void getArrivalStringReturnsCorrectDateAndTimeString() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertEquals(flight.getArrivalString(), "7/29/26, 10:00 AM");
  }

  /**
   * This unit test is to validate that the checkSourceOrDest method throws an exception if the input is empty.
   */
  @Test
  void checkSourceorDestWithEmptyInputThrowsException() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertThrows(IllegalArgumentException.class, () -> flight.checkSourceOrDest("")); 
  }

  /**
   * This unit test is to validate that the checkSourceOrDest method throws an exception if the input is not alphabetical.
   */
  @Test
  void checkSourceorDestWithNoAlphabeticalCharsThrowsException() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertThrows(IllegalArgumentException.class, () -> flight.checkSourceOrDest("123")); 
  }

  /**
   * This unit test is to validate that the checkSourceOrDest method throws an exception if the input is not fully alphabetical.
   */
  @Test
  void checkSourceorDestWithLettersAndNumbersThrowsException() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertThrows(IllegalArgumentException.class, () -> flight.checkSourceOrDest("1AA")); 
  }

  /**
   * This unit test is to validate that the checkSourceOrDest method returns true with valid input.
   */
  @Test
  void checkSourceorDestWithValidCharactersReturnsTrue() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight = new Flight(123, "PDX", source, "OAK", dest);
    assertEquals(flight.checkSourceOrDest("PDX"), true); 
  }

  /**
   * This unit test is to validate that compareTo returns -1 if current object goes before other object
   */
  @Test
  void compareToReturnsNegativeNumIfCurrentObjectComesBeforeOtherObject() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight1 = new Flight(123, "PDX", source, "OAK", dest);
    Flight flight2 = new Flight(123, "XXX", source, "OAK", dest);
    assertEquals(flight1.compareTo(flight2), -1);
  }

  /**
   * This unit test is to validate that compareTo returns 1 if current object goes after other object
   */
  @Test
  void compareToReturnsPositiveNumIfCurrentObjectComesBeforeOtherObject() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight1 = new Flight(123, "XXX", source, "OAK", dest);
    Flight flight2 = new Flight(123, "PDX", source, "OAK", dest);
    assertEquals(flight1.compareTo(flight2), 1);
  }

  /**
   * This unit test is to validate that compareTo returns 0 if current object equals other object
   */
  @Test
  void compareToReturnsPositiveNumIfCurrentObjectEqualsOtherObject() {
    LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight flight1 = new Flight(123, "PDX", source, "OAK", dest);
    Flight flight2 = new Flight(123, "PDX", source, "OAK", dest);
    assertEquals(flight1.compareTo(flight2), 0);
  }
  
  /**
   * This unit test is to validate that compareTo returns -2 if current object equals other object but their departure time is different
   */
  @Test
  void compareToReturnsNegativeNumIfCurrentObjectDepartureTimeComesBeforeOtherObject() {
    LocalDateTime source1 = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime source2 = LocalDateTime.of(2026, 7, 29, 9, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);
    
    Flight flight1 = new Flight(123, "PDX", source1, "OAK", dest);
    Flight flight2 = new Flight(123, "PDX", source2, "OAK", dest);
    assertEquals(flight1.compareTo(flight2), -2);
  }

  /**
   * This unit test is to validate that compareTo returns -2 if current object equals other object but their departure time is different
   */
  @Test
  void compareToReturnsNegativeNumIfCurrentObjectDepartureTimeComesAfterOtherObject() {
    LocalDateTime source1 = LocalDateTime.of(2026, 7, 29, 8, 0);
    LocalDateTime source2 = LocalDateTime.of(2026, 7, 29, 7, 0);
    LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);
    
    Flight flight1 = new Flight(123, "PDX", source1, "OAK", dest);
    Flight flight2 = new Flight(123, "PDX", source2, "OAK", dest);
    assertEquals(flight1.compareTo(flight2), 3);
  }
}
