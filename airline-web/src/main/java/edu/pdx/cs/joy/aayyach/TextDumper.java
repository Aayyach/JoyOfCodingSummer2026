package edu.pdx.cs.joy.aayyach;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;

/**
 * Implementation of the <code>TextDumper</code> class for Project 4.
 */
public class TextDumper {
  private final Writer writer;

  /**
   * Initalizes the writer field 
   * 
   * @param writer  The buffer that is being written to
   */
  public TextDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * Dumps an airline to stdout
   *
   * @param airline The airline being written to stdout
   */
  public void dump(Airline airline) {
    Collection<Flight> airlineFlights = airline.getFlights();
    try (PrintWriter pw = new PrintWriter(this.writer)) {
      pw.println(airline.getName());

      for (Flight flight : airlineFlights ) {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("M/d/yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm");
        DateTimeFormatter ext = DateTimeFormatter.ofPattern("a");
        LocalDateTime dept = flight.getDeparture();
        LocalDateTime arr = flight.getArrival(); 
        pw.println(flight.getNumber() + "," + flight.getSource() + "," + dept.format(date) + 
                   "," + dept.format(time) + "," + dept.format(ext) + "," + flight.getDestination() 
                   + "," + arr.format(date) + "," + arr.format(time) + "," + arr.format(ext));
      }
      pw.flush();
    }
  }
}
