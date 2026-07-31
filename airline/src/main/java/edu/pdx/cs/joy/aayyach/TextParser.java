package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.AirlineParser;
import edu.pdx.cs.joy.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementation of the <code>TextParser</code> class for Project 2.
 */
public class TextParser implements AirlineParser<Airline> {
  private final Reader reader;

  /**
   * Initalizes the reader field 
   * 
   * @param reader  The file that is being read
   */
  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parses a text file to see if an airline and flight(s) exist
   * Checks that the text file is formatted correctly 
   * 
   * @return Airline object from parsed file 
   */
  @Override
  public Airline parse() throws ParserException {
    try (BufferedReader br = new BufferedReader(this.reader)) {
      Airline airline = null;
      String airlineName = br.readLine();
      String line = "";

      if (airlineName == null) {
        throw new ParserException("Airline name is missing in the text file");
      }
      else {
        airline = new Airline(airlineName); 
        while ((line = br.readLine()) != null) {
          String [] flightInfo = line.split(","); 
          if (flightInfo.length != 9) {
            throw new ParserException("Text file is not formatted correctly (CSV)"); 
          }

          int num = 0;
          try {
            num = Integer.parseInt(flightInfo[0]);
          } catch (NumberFormatException e) {
            throw new ParserException("Text file is not formatted correctly (Flight number is not numerical)");
          }
          String src = flightInfo[1];
          String srcDateTime = flightInfo[2] + " " + flightInfo[3] + " " + flightInfo[4];
          String dest = flightInfo[5];
          String destDateTime = flightInfo[6] + " " + flightInfo[7] + " " + flightInfo[8];

          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
          LocalDateTime source = LocalDateTime.parse(srcDateTime, formatter);
          LocalDateTime destination = LocalDateTime.parse(destDateTime, formatter);

          Flight flight = new Flight(num, src, source, dest, destination); 
          airline.addFlight(flight);
        }
      }
      return airline;
    } catch (IOException e) {
      throw new ParserException("While parsing airline text", e);
    }
  }

}
