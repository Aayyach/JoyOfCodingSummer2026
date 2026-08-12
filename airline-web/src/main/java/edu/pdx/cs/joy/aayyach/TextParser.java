package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Implementation of the <code>TextParser</code> class for Project 5.
 */
public class TextParser {
  private final Reader reader;

  /**
   * Initalizes the reader field 
   * 
   * @param reader  The buffer that is being read
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parses a buffer to see if an airline and flight(s) exist
   * 
   * @return Airline object from parsed buffer
   */
  public Airline parse() throws ParserException {
    try (BufferedReader br = new BufferedReader(this.reader)) {
      Airline airline = null;
      Flight flight = null; 
      String airlineName = br.readLine();
      String line = "";

      airline = new Airline(airlineName); 
      while ((line = br.readLine()) != null) {
        String [] flightInfo = line.split(","); 
        if (flightInfo.length != 9) {
          throw new ParserException("Expected 9 arguments to be passed in to parse."); 
        }

        int num = 0;
        try {
          num = Integer.parseInt(flightInfo[0]);
        } catch (NumberFormatException e) {
          throw new ParserException("Flight number is not numerical.");
         }

        String src = flightInfo[1];
        String srcDateTime = flightInfo[2] + " " + flightInfo[3] + " " + flightInfo[4];
        String dest = flightInfo[5];
        String destDateTime = flightInfo[6] + " " + flightInfo[7] + " " + flightInfo[8];
        LocalDateTime source = null;
        LocalDateTime destination = null;
          
        try {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
          source = LocalDateTime.parse(srcDateTime, formatter);
          destination = LocalDateTime.parse(destDateTime, formatter);
        } catch (DateTimeParseException e) {
          throw new ParserException("Error while parsing date and time.");
        }

        if (source != null && destination != null) {
          flight = new Flight(num, src, source, dest, destination); 
          airline.addFlight(flight);
        }
      } 
      return airline;
    } catch (IOException e) {
        throw new ParserException("While parsing airline", e);
    }
  }
}