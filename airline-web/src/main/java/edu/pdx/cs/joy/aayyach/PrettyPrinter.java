package edu.pdx.cs.joy.aayyach;

import com.google.common.annotations.VisibleForTesting;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.*;

public class PrettyPrinter {
  private final Writer writer;

  @VisibleForTesting
  static String formatWordCount(int count )
  {
    return String.format( "Dictionary on server contains %d words", count );
  }

  @VisibleForTesting
  static String formatDictionaryEntry(String word, String definition )
  {
    return String.format("  %s -> %s", word, definition);
  }


  public PrettyPrinter(Writer writer) {
    this.writer = writer;
  }

  public void dump(Airline airline) {
    try (
      PrintWriter pw = new PrintWriter(this.writer)
    ) {

      Collection<Flight> flights = airline.getFlights();
      pw.println(airline.getName() + " with " + flights.size() + " flights\n");
      
      for (Flight flight : flights) {
        pw.printf(" Flight number: %s\n", flight.getNumber());
        pw.printf(" Departure airport: %s\n", flight.getDeparture());
        pw.printf(" Departure time: %s\n", flight.getDepartureString());
        pw.printf(" Arrival airport: %s\n", flight.getArrival());
        pw.printf(" Arrival time: %s\n", flight.getArrivalString());
      }

      pw.flush();
    }

  }
}
