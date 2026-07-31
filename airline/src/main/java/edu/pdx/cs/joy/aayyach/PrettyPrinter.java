package edu.pdx.cs.joy.aayyach;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import edu.pdx.cs.joy.AirlineDumper;
import edu.pdx.cs.joy.AirportNames;

public class PrettyPrinter implements AirlineDumper<Airline> {
    private final Writer writer;

    /**
     * Initalizes the writer field 
     * 
     * @param writer  The file that is being written to
     */
    public PrettyPrinter(Writer writer) {
        this.writer = writer;
        if (writer == null) { throw new NullPointerException(); }
    }

    /**
     * Pretty prints an airlines flights to standard out or a text file
     * 
     * @param airline   The airline object being written to a file or stdout
     */
    @Override
    public void dump(Airline airline) {
        String airlineName = airline.getName();
        Collection<Flight> flights = airline.getFlights();
        PrintWriter pw = new PrintWriter(this.writer);
        pw.println(airlineName + "\n");
        for (Flight flight : flights ) {
            DateTimeFormatter date = DateTimeFormatter.ofPattern("M/d/yyyy");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm");
            DateTimeFormatter ext = DateTimeFormatter.ofPattern("a");
            LocalDateTime dept = flight.getDeparture();
            LocalDateTime arr = flight.getArrival(); 
            long duration = Duration.between(dept, arr).toMinutes();
            pw.println("Flight #: " + flight.getNumber());
            pw.println("Flight Duration: " + duration + " minutes");
            pw.println("Departure: " + AirportNames.getName(flight.getSource()) + " " + "(" + flight.getSource() + ")");
            pw.println("Departure Time: " + dept.format(date) + " " + dept.format(time) + dept.format(ext));
            pw.println("Arrival: " + AirportNames.getName(flight.getDestination()) + " " + "(" + flight.getDestination() + ")");
            pw.println("Arrival Time: " + arr.format(date) + " " + arr.format(time) + arr.format(ext));
        }
        pw.flush();
    }
}
