package edu.pdx.cs.joy.aayyach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

/**
 * Unit tests for the {@link Airline} class.
 */
public class AirlineTest {

/**
 * This unit test checks to make sure that the default constructor of Airline works as expected.
 */
@Test 
void checkDefaultConstructorOfAirline() {
   Airline test = new Airline();
   assertEquals(test.getName(), "");
   assertEquals(test.getFlights(), null);
}

/**
 * This unit test checks that the checkName method throws an exception for empty input in the name field.
 */
@Test 
void checkNameWithInvalidInputThrowsException() {
   Airline test = new Airline("Test");
   assertThrows(IllegalArgumentException.class, () -> test.checkName("")); 
}

/**
 * This unit test checks that the checkName method returns true for valid input (any non-empty input).
 */
@Test
void checkNameWithValidInputReturnsTrue() {
   Airline test = new Airline("Test");
   assertEquals(test.checkName("Test"), true);
}

/**
 * This unit test checks that the getName method returns the correct Airline name
 */
 @Test
 void getNameReturnsAirlineName() {
    Airline test = new Airline("Test");
    assertEquals(test.getName(), "Test");
 }

 /**
 * This unit test checks that the addFlight method correctly adds the Flight object 
 * to the flights list and that getFlight returns the correct flights list. 
 */
 @Test 
 void getFlightsReturnsFlightsCollectionAfterAddingFlight() {
    Airline test = new Airline("Test");
   LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
   LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

    Flight testFlight = new Flight(123, "PDX", source, "OAK", dest);
    test.addFlight(testFlight);
    assertEquals(test.getFlights().toArray()[0], testFlight);
 }

}
