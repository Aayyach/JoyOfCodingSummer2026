package edu.pdx.cs.joy.aayyach;

import com.google.common.annotations.VisibleForTesting;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.*;

public class PrettyPrinter {
  private final Writer writer;

  /**
   * Formats word count
   */
  @VisibleForTesting
  static String formatWordCount(int count )
  {
    return String.format( "Dictionary on server contains %d words", count );
  }

  /**
   * Formats passed in arguments
   */
  @VisibleForTesting
  static String formatDictionaryEntry(String word, String definition )
  {
    return String.format("  %s -> %s", word, definition);
  }


  /**
   * Initalizes the writer field 
   * 
   * @param writer The file that is being written to
   */
  public PrettyPrinter(Writer writer) {
    this.writer = writer;
  }

  /**
   * Pretty prints an airlines flights to standard out or a text file
   * 
   * @param airline The airline object being written to a file or stdout
   */
  public void dump(Airline airline) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ) {

      Collection<Flight> flights = airline.getFlights();
      if (flights.size() > 0) { pw.println(airline.getName() + " with " + flights.size() + " flights\n"); }
      else { pw.println("The airline \"" + airline.getName() + "\"" + " has no flights with the specified source and destination airports provided in the command line\n"); }
      
      for (Flight flight : flights) {
        pw.printf(" Flight number: %s\n", flight.getNumber());
        pw.printf(" Departure airport: %s\n", flight.getSource());
        pw.printf(" Departure time: %s\n", flight.getDepartureString());
        pw.printf(" Arrival airport: %s\n", flight.getDestination());
        pw.printf(" Arrival time: %s\n", flight.getArrivalString());
        pw.printf("\n");
      }

      pw.flush();
    }

  }
}
