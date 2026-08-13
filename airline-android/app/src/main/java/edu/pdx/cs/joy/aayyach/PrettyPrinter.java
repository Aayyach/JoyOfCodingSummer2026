package edu.pdx.cs.joy.aayyach;

import android.widget.TextView;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;

public class PrettyPrinter {
    private final TextView writer;

    public PrettyPrinter(TextView writer) {
        this.writer = writer;
    }

    public void dump(Airline airline) {

        Collection<Flight> flights = airline.getFlights();
        if (!flights.isEmpty()) { writer.setText("   " + airline.getName() + " has " + flights.size() + " flight(s)\n\n"); }
        else { writer.setText(" The airline \"" + airline.getName() + "\"" + " has no flights with the specified source and destination airports provided.\n"); }

        for (Flight flight : flights) {
            writer.append("   Flight number: " + flight.getNumber() + "\n");
            writer.append("   Departure airport: " + flight.getSource() + "\n");
            writer.append("   Departure time: " + flight.getDepartureString() + "\n");
            writer.append("   Arrival airport: " + flight.getDestination() + "\n");
            writer.append("   Arrival time: " + flight.getArrivalString() + "\n");
            writer.append("\n");
        }
    }

}