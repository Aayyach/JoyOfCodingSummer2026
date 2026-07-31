package edu.pdx.cs.joy.aayyach;

import java.time.LocalDateTime;
import java.time.format.FormatStyle;
import java.time.format.DateTimeFormatter;

import edu.pdx.cs.joy.AbstractFlight;

/**
 * This class represents an airline flight, which extends the AbstractFlight class.  Each flight has a unique
 * number identifying it, an origin airport identified by the
 * airport's three-letter code, a departure time, a destination
 * airport identified by the airport's three-letter code, and an
 * arrival time.
 */
public class Flight extends AbstractFlight implements Comparable<Flight> {
  private final int number;  
  private final String source;
  private final LocalDateTime sourceDateTime; 
  private final String dest;
  private final LocalDateTime destDateTime; 

  /**
   * Default constructor that initializes the fields of this flight to their default equivalent.
   */
  public Flight() {
    this.number = 0;
    this.source = "";
    this.sourceDateTime = null;
    this.dest = "";
    this.destDateTime = null;
  }

  /**
   * Initalizes the fields of this airline.
   * 
   * @param number  the flight number
   * @param source  the name of the departing airport
   * @param sourceDateTime  the date and time of the departing flight
   * @param dest  the name of the arriving airport
   * @param destDateTime  the date and time of the arriving flight
   */
  public Flight(int number, String source, LocalDateTime sourceDateTime, String dest, LocalDateTime destDateTime) {
    // Uses methods to check for valid input from parameters 
    checkSourceOrDest(source);
    checkSourceOrDest(dest);

    this.number = number; 
    this.source = source.toUpperCase(); 
    this.sourceDateTime = sourceDateTime;
    this.dest = dest.toUpperCase();
    this.destDateTime = destDateTime;
  }

  /**
   * Checks if the source or dest field is of a valid length and character type. Throws an IllegalArgumentException in the case that it is not valid. 
   * 
   * @param field  the string value of the source/dest airport 
   * @return  Boolean value (true) if source is valid
   */
  public Boolean checkSourceOrDest(String field) {
    // Checking if source is not 3 characters (Airports are defined with three letters)
    if (field.length() != 3) {
      throw new IllegalArgumentException("Departure and arrival airports must contain only alphabetical characters and be three characters in length. Please try running the program again with the appropriate airport codes."); 
    }
    
    // Checks if field only has alphabetical characters and throws an IllegalArgumentException if not
    for (int i = 0; i < field.length(); i++) {
      char oneChar = field.charAt(i); 
      if (!Character.isAlphabetic(oneChar)) {
        throw new IllegalArgumentException("Departure and arrival airports must contain only alphabetical characters and be three characters in length. Please try running the program again with the appropriate airport codes.");
      }
    }

    return true; 
  }
 
  /**
   * Returns the flight number of this flight. 
   */
  @Override
  public int getNumber() {
    return this.number; 
  }

  /**
   * Returns the source airport of this flight. 
   */
  @Override
  public String getSource() {
    return this.source; 
  }

  /**
   * Returns this flight's departure time as a <code>Date</code>.
   */
  @Override
  public LocalDateTime getDeparture() {
    return this.sourceDateTime;
  }

  /**
   * Returns the departure date and time of this flight.
   */
  @Override
  public String getDepartureString() {
    LocalDateTime dateTime = this.sourceDateTime; 
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    return dateTime.format(formatter).replace("\u202F", " ");
  }

  /**
   * Returns the destination airport of this flight.
   */
  @Override
  public String getDestination() {
    return this.dest;
  }

  /**
   * Returns this flight's arrival time as a <code>Date</code>.
   */
  @Override
  public LocalDateTime getArrival() {
    return this.destDateTime;
  }

  /**
   * Returns the arrival date and time of this flight.
   */
  @Override
  public String getArrivalString() {
    LocalDateTime dateTime = this.destDateTime; 
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    return dateTime.format(formatter).replace("\u202F", " ");
  }

  /**
   * Sorts flights alphabetically by the source code 
   * 
   * @param other   a flight object
   * @return  integer value depending on comparison 
   */
  @Override
  public int compareTo(Flight other) {
    // Comparison logic
    if (other == null) {
      throw new NullPointerException("Cannot compare: Flight object is null");
    }

    int result = this.source.compareToIgnoreCase(other.getSource());
    // this is before other
    if (result < 0) {
      return -1;
    } 
    // this is after other
    else if (result > 0) {
      return 1;
    }
    // this and other are equal
    else {
      // this and other departure time is equal
      if (this.sourceDateTime.isEqual(other.getDeparture())) {
        return 0;
      }
      // this departure time is before other's
      else if (this.sourceDateTime.isBefore(other.getDeparture())) {
        return -2;
      }
      // this departure time is after other's
      else {
        return 3; 
      }
    }
  }

}
