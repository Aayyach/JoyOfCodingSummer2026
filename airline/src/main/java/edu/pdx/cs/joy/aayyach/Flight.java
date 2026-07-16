package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.AbstractFlight;

/**
 * This class represents an airline flight, which extends the AbstractFlight class.  Each flight has a unique
 * number identifying it, an origin airport identified by the
 * airport's three-letter code, a departure time, a destination
 * airport identified by the airport's three-letter code, and an
 * arrival time.
 */
public class Flight extends AbstractFlight {
  private final String source;
  private final String sourceDate; 
  private final String sourceTime; 
  private final String dest;
  private final String destDate;
  private final String destTime; 
  private final int number;  

  /**
   * Default constructor that initializes the fields of this flight to their default equivalent.
   */

  public Flight() {
    this.source = "";
    this.sourceDate = "";
    this.sourceTime = "";
    this.dest = "";
    this.destDate = "";
    this.destTime = "";
    this.number = 0;
  }

  /**
   * Initalizes the fields of this airline.
   * 
   * @param number  the flight number
   * @param source  the name of the departing airport
   * @param sourceDate  the date of the departing flight
   * @param sourceTime  the time of the departing flight
   * @param dest  the name of the arrival airport
   * @param destDate  the date of the arrival
   * @param destTime  the time of the arrival
   */
  public Flight(int number, String source, String sourceDate, String sourceTime, String dest, String destDate, String destTime) {
    // Uses methods to check for valid input from parameters 
    checkSourceOrDest(source);
    checkSourceOrDest(dest);
    checkDate(sourceDate);
    checkDate(destDate);
    checkTime(sourceTime);
    checkTime(destTime);

    this.number = number; 
    this.source = source; 
    this.sourceDate = sourceDate;
    this.sourceTime = sourceTime; 
    this.dest = dest;
    this.destDate = destDate;
    this.destTime = destTime; 
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
   * Checks if the sourceDate/destDate is of a valid length and format. Throws an IllegalArgumentException in the case that it is not valid.
   * 
   * @param date  the date string of the departure/arrival
   * @return  Boolean value (true) if the date is valid
   */
  public Boolean checkDate(String date) {
    if (date.length() < 8) {
      throw new IllegalArgumentException("Date must be in the format \"mm/dd/yyyy\" or \"m/d/yyyy\" with only numerical characters for mm, dd, and yyyy. Please try running the program again with a date in the specified formats."); 
    }
    // Checks if the string is in the correct format with regex
    else if (!date.matches("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$")) {
      throw new IllegalArgumentException("Date must be in the format \"mm/dd/yyyy\" or \"m/d/yyyy\" with only numerical characters for mm, dd, and yyyy. Please try running the program again with a date in the specified formats.");
    }

    return true; 
  }

  /**
   * Checks if the sourceTime/sourceTime is of a valid length and format. Throws an IllegalArgumentException in the case that it is not valid.
   * 
   * @param time  the time string of the departure/arrival
   * @return  Boolean value (true) if the time is valid
   */
  public Boolean checkTime(String time) {
    if (time.length() < 4) {
      throw new IllegalArgumentException("Time must be in the format \"hh:mm\" or \"h:mm\" with only numerical characters for hh and mm and be 4 or 5 characters in length. Please try running the program again with a time in the specified formats.");
    }
    // Checks if the string is in the correct format with regex
    else if (!time.matches("^[0-9]{1,2}:[0-9]{2}$")) {
      throw new IllegalArgumentException("Time must be in the format \"hh:mm\" or \"h:mm\" with only numerical characters for hh and mm and be 4 or 5 characters in length. Please try running the program again with a time in the specified formats.");
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
   * Returns the departure date and time of this flight.
   */
  @Override
  public String getDepartureString() {
    return this.sourceDate + " " + this.sourceTime;
  }

  /**
   * Returns the destination airport of this flight.
   */
  @Override
  public String getDestination() {
    return this.dest;
  }

  /**
   * Returns the arrival date and time of this flight.
   */
  @Override
  public String getArrivalString() {
    return this.destDate + " " + this.destTime;
  }
}
