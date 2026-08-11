package edu.pdx.cs.joy.aayyach;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import edu.pdx.cs.joy.web.HttpRequestHelper.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.io.StringReader;
import java.util.Map;

import static edu.pdx.cs.joy.web.HttpRequestHelper.*;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class AirlineRestClient
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final HttpRequestHelper http;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * 
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient(String hostName, int port)
    {
        this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
    }

    /**
     * AirlineRestClient constructor that only accepts the http parameter
     * 
     * @param http the http parameter
     */
    @VisibleForTesting
    AirlineRestClient(HttpRequestHelper http) {
      this.http = http;
    }

  /**
   * Returns airline and flight information in the form of an airline object
   */
  public Airline getAirlineAndFlight(String airlineName, String src, String dest) throws IOException, ParserException {
    if (src == null || dest == null) {
      Response response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName));
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      TextParser parser = new TextParser(new StringReader(content));
      return parser.parse();
    } else if (src == null && dest != null) {
      Response response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.DEST_AIRPORT_PARAMETER, dest));
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      TextParser parser = new TextParser(new StringReader(content));
      return parser.parse();
    } else if (src != null && dest == null) {
      Response response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.SRC_AIRPORT_PARAMETER, src));
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      TextParser parser = new TextParser(new StringReader(content));
      return parser.parse();
    } else {
      Response response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.SRC_AIRPORT_PARAMETER, src, 
        AirlineServlet.DEST_AIRPORT_PARAMETER, dest));
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      TextParser parser = new TextParser(new StringReader(content));
      return parser.parse();
    }
  }

  /**
   * Returns the definition for the given airlineName
   * 
   * @param airlineName the name of the airline
   * @throws ParserException  throws a parser expcetion if the parser runs into an error
   * @throws IOException  throws an IOException 
   * @return an airline object
   */
  public Airline getAirline(String airlineName) throws IOException, ParserException {
    Response response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse();
  }

  /**
   * POST request to add a new flight to an airline
   * 
   * @param airlineName the name of the airline
   * @param flightNumber the string representation of the flight number
   * @throws IOException throws an IOException if HTTP status is not 200
   */
  public void addFlight(String airlineName, String flightNumber) throws IOException {
    Response response = http.post(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.FLIGHT_NUMBER_PARAMETER, flightNumber));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * POST request to add a new flight to an airline with more required arguments
   * 
   * @param airlineName the name of the airline
   * @param flightNumber the string representation of the flight number
   * @param source the source airport
   * @param departTime the departure time
   * @param dest the destination airport
   * @param arrivalTime the arrival time
   * @throws IOException throws an IOException if HTTP status is not 200
   */
  public void addFlight(String airlineName, String flightNumber, String source, String departTime, String dest, String arrivalTime) throws IOException {
    Response response = http.post(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.FLIGHT_NUMBER_PARAMETER, flightNumber, 
        AirlineServlet.SRC_AIRPORT_PARAMETER, source, AirlineServlet.DEPART_TIME_PARAMETER, departTime, AirlineServlet.DEST_AIRPORT_PARAMETER, dest,
        AirlineServlet.ARRIVAL_TIME_PARAMETER, arrivalTime));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * DELETE request to delete all airlines and their corresponding flights
   * 
   * @throws IOException throws an IOException if HTTP status is not 200
   */
  public void removeAllAirlines() throws IOException {
    Response response = http.delete(Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * SEARCH request to search for an airline with an optional specified SRC and DEST airport
   * 
   * @throws IOException throws an IOException if HTTP status is not 200
   * @throws ParserException throws a ParserException if TextParser encounters an error
   */
    public Airline searchForAirlines(String airlineName, String src, String dest) throws IOException, ParserException {
      Response response; 
      if (src.equals("NONE") && dest.equals("NONE")) {
        response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName));
      } else {
        response = http.get(Map.of(
            AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName,
            AirlineServlet.SRC_AIRPORT_PARAMETER, src,
            AirlineServlet.DEST_AIRPORT_PARAMETER, dest));
      }
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      TextParser parser = new TextParser(new StringReader(content));
      return parser.parse();
    }

  /**
   * Throws an exception if HTTP status is not 200
   * 
   * @param response  the HTTP response
   */
  private void throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }

}
