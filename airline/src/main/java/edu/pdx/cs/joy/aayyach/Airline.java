package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.AbstractAirline;

import java.util.Collection;
import java.util.ArrayList; 

/**
 * This class represents an airline, which extends the AbstractAirline class. Each airline has a name and
 * consists of multiple flights.
 */
public class Airline extends AbstractAirline<Flight> {
  private final String name;
  private final Collection<Flight> flights;

  /**
   * Initializes the fields of this airline.
   * 
   * @param name  the name of the airline
   */
  public Airline(String name) {
    checkName(name);

    this.name = name;
    this.flights = new ArrayList<Flight>(); 
  }

  /**
   * Checks if the name of the airline is valid. 
   * 
   * @param name  the name of the airline
   * @return  Boolean value (true) if the name is valid (not empty).
   */
  public Boolean checkName(String name) {
    if (name.isEmpty()) { throw new IllegalArgumentException("The name of the Airline cannot be left empty. Please try running the program again with an airline name that has 1 or more characters."); }
    return true;
  }

  /**
   * Returns the name of this airline.
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Adds flight object to the flights field in this airline using add method for Collections.
   * 
   * @param flight  the flight object being added to the flights list
   */
  @Override
  public void addFlight(Flight flight) {
    flights.add(flight); 
  }

  /**
   * Returns the flights collection of this airline. 
   */
  @Override
  public Collection<Flight> getFlights() {
    return this.flights;
  }
}
