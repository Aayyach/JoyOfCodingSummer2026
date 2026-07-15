package edu.pdx.cs.joy.aayyach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Airline} class.
 */
public class AirlineTest {

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

    Flight testFlight = new Flight("PDX", "07/14/2026", "20:00", "OAK", "07/14/2026", "22:00", 123);
    test.addFlight(testFlight);
    assertEquals(test.getFlights().toArray()[0], testFlight);
 }

}
