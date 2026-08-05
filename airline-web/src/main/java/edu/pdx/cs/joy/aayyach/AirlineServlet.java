package edu.pdx.cs.joy.aayyach;

import com.google.common.annotations.VisibleForTesting;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Collection;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>. 
 */
public class AirlineServlet extends HttpServlet {
  static final String AIRLINE_NAME_PARAMETER = "airline";
  static final String FLIGHT_NUMBER_PARAMETER = "flightNumber";
  static final String SRC_AIRPORT_PARAMETER = "src";
  static final String DEPART_TIME_PARAMETER = "depart";
  static final String DEST_AIRPORT_PARAMETER = "dest";
  static final String ARRIVAL_TIME_PARAMETER = "arrive";

  private final Map<String, Airline> airlines = new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by writing an airline of the to the HTTP response. If the
   * "airlineName" parameter is not specified, all of the entries in the airlines
   * are written to the HTTP response.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      response.setContentType("text/plain");

      // Attempts to get value of airline from the URL 
      String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request);
      String source = getParameter(SRC_AIRPORT_PARAMETER, request);
      String dest = getParameter(DEST_AIRPORT_PARAMETER, request);
      if (airlineName != null && source != null && dest != null) {
        log("GET " + airlineName + " Source: " + source + " Dest: " + dest);
        writeFilteredAirline(airlineName, source, dest, response);
      } else if (airlineName != null) {
          log("GET " + airlineName);
          // Dumps the data to the response
          writeAirline(airlineName, response);
      } else {
        missingRequiredParameter(response, AIRLINE_NAME_PARAMETER); 
      }
  }

  /**
   * Handles an HTTP POST request by creating a new flight in the airline
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      response.setContentType("text/plain");

      // Checks if the airline name is valid (not null)
      String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request);
      if (airlineName == null) {
          missingRequiredParameter(response, AIRLINE_NAME_PARAMETER);
          return;
      }

      // Checks if the flight number is valid
      String flightNumberString = getParameter(FLIGHT_NUMBER_PARAMETER, request);
      if (flightNumberString == null) {
          missingRequiredParameter(response, FLIGHT_NUMBER_PARAMETER);
          return;
      }

      String source = getParameter(SRC_AIRPORT_PARAMETER, request);
      if (source == null) {
          missingRequiredParameter(response, SRC_AIRPORT_PARAMETER);
          return;
      }

      String departTime = getParameter(DEPART_TIME_PARAMETER, request);
      if (departTime == null) {
        missingRequiredParameter(response, DEPART_TIME_PARAMETER);
        return;
      }

      String destination = getParameter(DEST_AIRPORT_PARAMETER, request);
      if (destination == null) {
        missingRequiredParameter(response, DEST_AIRPORT_PARAMETER);
        return;
      }

      String arrivalTime = getParameter(ARRIVAL_TIME_PARAMETER, request);
      if (arrivalTime == null) {
        missingRequiredParameter(response, ARRIVAL_TIME_PARAMETER);
        return;
      }

      log("POST " + airlineName + " -> " + flightNumberString);

      Airline airline = this.airlines.get(airlineName);
      // Creates airline if it doesn't exist and stores it into the airlines hashmap
      if ( airline == null ) {
        airline = new Airline(airlineName);
        this.airlines.put(airlineName, airline);
      }
      int flightNumber = Integer.parseInt(flightNumberString);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
      LocalDateTime depart = LocalDateTime.parse(departTime, formatter);
      LocalDateTime arrival = LocalDateTime.parse(arrivalTime, formatter);
      airline.addFlight(new Flight(flightNumber, source, depart, destination, arrival));
      

      PrintWriter pw = response.getWriter();
      pw.println(Messages.definedAirlineNameAs(airlineName, flightNumberString));
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all airlines.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("text/plain");

      log("DELETE all airlines entries");

      this.airlines.clear();

      PrintWriter pw = response.getWriter();
      pw.println(Messages.allAirlinesDeleted());
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);

  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes the airline of the given airlineName to the HTTP response.
   *
   * The text of the message is formatted with {@link TextDumper}
   */
  private void writeAirline(String airlineName, HttpServletResponse response) throws IOException {
    Airline airline = this.airlines.get(airlineName);

    if (airline == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } else {
      PrintWriter pw = response.getWriter();

      TextDumper dumper = new TextDumper(pw);
      dumper.dump(airline);

      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  /**
   * Writes the filtered airline to the HTTP response
   */
  private void writeFilteredAirline(String airlineName, String source, String dest, HttpServletResponse response) throws IOException {
    Airline filtered = filter(airlineName, source, dest); 
    if (filtered == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      PrintWriter pw = response.getWriter();

      TextDumper dumper = new TextDumper(pw);
      dumper.dump(filtered);

      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  @VisibleForTesting
  Airline getAirline(String airlineName) {
      return this.airlines.get(airlineName);
  }

  @Override
  public void log(String msg) {
    System.out.println(msg);
  }

  /**
   * Filters all airline flights that originate at the src airpiort and terminate at the dest airport
   * 
   * @param airlineName the airline name
   * @param source the source airport
   * @param dest the destination airport
   * @return the filtered airline object
   */
  public Airline filter(String airlineName, String source, String dest) {
    Airline airline = this.airlines.get(airlineName);
    if (airline == null) {
      return null;
    }

    Airline filtered = new Airline(airline.getName());
    Collection<Flight> flights = airline.getFlights();
    for (Flight flight : flights) {
      if (flight.getSource().equalsIgnoreCase(source) && flight.getDestination().equalsIgnoreCase(dest)) {
        filtered.addFlight(flight);
      }
    }
    return filtered;
  }
}
