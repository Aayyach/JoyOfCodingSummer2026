package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.AirlineDumper;

import java.io.Writer;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Implementation of the <code>TextDumper</code> class for Project 2.
 */
public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;

  /**
   * Initalizes the writer field 
   * 
   * @param writer  The file that is being written to
   */
  public TextDumper(Writer writer) {
    this.writer = writer;
    if (writer == null) { throw new NullPointerException(); }
  }

  /**
   * Dumps an airline to some destination
   *
   * @param airline   The airline being written to the file
   */
  @Override
  public void dump(Airline airline) {
    Collection<Flight> airlineFlights = airline.getFlights();
    try (PrintWriter pw = new PrintWriter(this.writer)) {
      pw.println(airline.getName());
      // Writes each flight in the flights collection in the format flightNumber, src, depart, dest, arrive\n
      for (Flight flight : airlineFlights ) {
        pw.println(flight.getNumber() + "," + flight.getSource() + "," + flight.getDepartureString() + 
                 "," + flight.getDestination() + "," + flight.getArrivalString());
      }
      pw.flush();
    }
  }
}
