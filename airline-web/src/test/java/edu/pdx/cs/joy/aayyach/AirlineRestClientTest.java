package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * A unit test for the REST client that demonstrates using mocks and
 * dependency injection
 */
public class AirlineRestClientTest {

  /**
   * Unit test to check that the GET request is working
   */
  @Test
  void getAirlinePerformsHttpGetWithAirlineNameParameter() throws ParserException, IOException {
    String airlineName = "Airline";
    Airline airline = new Airline(airlineName);
    int flightNumber = 123;
    String src = "PDX";
    String srcDateTime = "08/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "08/06/2026 12:00 AM";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
    LocalDateTime source = LocalDateTime.parse(srcDateTime, formatter);
    LocalDateTime destination = LocalDateTime.parse(destDateTime, formatter);
    airline.addFlight(new Flight(flightNumber, src, source, dest, destination));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName)))).thenReturn(airlineAsText(airline));
    
    AirlineRestClient client = new AirlineRestClient(http);

    Airline fetched = client.getAirline(airlineName);
    assertThat(fetched.getName(), equalTo(airline.getName()));
    assertThat(fetched.getFlights().size(), equalTo(1));
    assertThat(fetched.getFlights().iterator().next().getNumber(), equalTo(flightNumber));
  }

  /**
   * Unit test to check that the searchForAirlines method is working with no src or dest
   */
  @Test
  void searchForAirlinesWithNoSrcOrDest() throws ParserException, IOException {
    String airlineName = "Airline";
    String src = "NONE";
    String dest = "NONE";
    Airline airline = new Airline(airlineName); 

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName)))).thenReturn(airlineAsText(airline));

    AirlineRestClient client = new AirlineRestClient(http);

    Airline fetched = client.searchForAirlines(airlineName, src, dest);
    assertThat(fetched.getName(), equalTo(airline.getName()));
    assertThat(fetched.getFlights().size(), equalTo(0));
  }

  /**
   * Unit test to check that the searchForAirlines method is working with all params
   */
  @Test
  void searchForAirlinesWithAllParams() throws ParserException, IOException {
    String airlineName = "Airline";
    Airline airline = new Airline(airlineName); 
    int flightNumber = 123;
    String src = "PDX";
    String srcDateTime = "08/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "08/06/2026 12:00 AM";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
    LocalDateTime source = LocalDateTime.parse(srcDateTime, formatter);
    LocalDateTime destination = LocalDateTime.parse(destDateTime, formatter);
    airline.addFlight(new Flight(flightNumber, src, source, dest, destination));
    airline.addFlight(new Flight(223, "PDX", source, "LAX", destination));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.SRC_AIRPORT_PARAMETER, src, AirlineServlet.DEST_AIRPORT_PARAMETER, dest)))).thenReturn(airlineAsText(airline));

    AirlineRestClient client = new AirlineRestClient(http);

    Airline fetched = client.searchForAirlines(airlineName, src, dest);
    assertThat(fetched.getName(), equalTo(airline.getName()));
    assertThat(fetched.getFlights().size(), equalTo(2));
    assertThat(fetched.getFlights().iterator().next().getSource(), equalTo(src));
    assertThat(fetched.getFlights().iterator().next().getDestination(), equalTo(dest));
  }

  /**
   * Unit test to check that the GET request is working for getAirlineAndFlight method
   */
  @Test
  void getAirlineAndFlightPerformsHttpGetWithAirlineNameParameter() throws ParserException, IOException {
    String airlineName = "Airline";
    Airline airline = new Airline(airlineName);
    int flightNumber = 123;
    String src = "PDX";
    String srcDateTime = "08/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "08/06/2026 12:00 AM";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
    LocalDateTime source = LocalDateTime.parse(srcDateTime, formatter);
    LocalDateTime destination = LocalDateTime.parse(destDateTime, formatter);
    airline.addFlight(new Flight(flightNumber, src, source, dest, destination));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName)))).thenReturn(airlineAsText(airline));
    
    AirlineRestClient client = new AirlineRestClient(http);

    Airline fetched = client.getAirlineAndFlight(airlineName, null, null);
    assertThat(fetched.getName(), equalTo(airline.getName()));
    assertThat(fetched.getFlights().size(), equalTo(1));
    assertThat(fetched.getFlights().iterator().next().getNumber(), equalTo(flightNumber));
  }

  /**
   * Unit test to check that the GET request is working for getAirlineAndFlight method
   */
  @Test
  void getAirlineAndFlightPerformsHttpGetWithAirlineNameParameterWithNoDestParam() throws ParserException, IOException {
    String airlineName = "Airline";
    Airline airline = new Airline(airlineName);
    int flightNumber = 123;
    String src = "PDX";
    String srcDateTime = "08/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "08/06/2026 12:00 AM";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
    LocalDateTime source = LocalDateTime.parse(srcDateTime, formatter);
    LocalDateTime destination = LocalDateTime.parse(destDateTime, formatter);
    airline.addFlight(new Flight(flightNumber, src, source, dest, destination));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.SRC_AIRPORT_PARAMETER, src)))).thenReturn(airlineAsText(airline));
    
    AirlineRestClient client = new AirlineRestClient(http);

    Airline fetched = client.getAirlineAndFlight(airlineName, src, null);
    assertThat(fetched.getName(), equalTo(airline.getName()));
    assertThat(fetched.getFlights().size(), equalTo(1));
    assertThat(fetched.getFlights().iterator().next().getNumber(), equalTo(flightNumber));
  }

  /**
   * Unit test to check that the GET request is working for getAirlineAndFlight method
   */
  @Test
  void getAirlineAndFlightPerformsHttpGetWithAirlineNameParameterWithNoSrcParam() throws ParserException, IOException {
    String airlineName = "Airline";
    Airline airline = new Airline(airlineName);
    int flightNumber = 123;
    String src = "PDX";
    String srcDateTime = "08/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "08/06/2026 12:00 AM";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
    LocalDateTime source = LocalDateTime.parse(srcDateTime, formatter);
    LocalDateTime destination = LocalDateTime.parse(destDateTime, formatter);
    airline.addFlight(new Flight(flightNumber, src, source, dest, destination));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.DEST_AIRPORT_PARAMETER, dest)))).thenReturn(airlineAsText(airline));
    
    AirlineRestClient client = new AirlineRestClient(http);

    Airline fetched = client.getAirlineAndFlight(airlineName, null, dest);
    assertThat(fetched.getName(), equalTo(airline.getName()));
    assertThat(fetched.getFlights().size(), equalTo(1));
    assertThat(fetched.getFlights().iterator().next().getNumber(), equalTo(flightNumber));
  }

  /**
   * Unit test to check that the GET request is working for getAirlineAndFlight method
   */
  @Test
  void getAirlineAndFlightPerformsHttpGetWithAirlineNameParameterWithAllParams() throws ParserException, IOException {
    String airlineName = "Airline";
    Airline airline = new Airline(airlineName);
    int flightNumber = 123;
    String src = "PDX";
    String srcDateTime = "08/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "08/06/2026 12:00 AM";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
    LocalDateTime source = LocalDateTime.parse(srcDateTime, formatter);
    LocalDateTime destination = LocalDateTime.parse(destDateTime, formatter);
    airline.addFlight(new Flight(flightNumber, src, source, dest, destination));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName, AirlineServlet.SRC_AIRPORT_PARAMETER, src, AirlineServlet.DEST_AIRPORT_PARAMETER, dest)))).thenReturn(airlineAsText(airline));
    
    AirlineRestClient client = new AirlineRestClient(http);

    Airline fetched = client.getAirlineAndFlight(airlineName, src, dest);
    assertThat(fetched.getName(), equalTo(airline.getName()));
    assertThat(fetched.getFlights().size(), equalTo(1));
    assertThat(fetched.getFlights().iterator().next().getNumber(), equalTo(flightNumber));
  }

  private HttpRequestHelper.Response airlineAsText(Airline airline) {
    StringWriter writer = new StringWriter();
    new TextDumper(writer).dump(airline);

    return new HttpRequestHelper.Response(writer.toString());
  }
}
