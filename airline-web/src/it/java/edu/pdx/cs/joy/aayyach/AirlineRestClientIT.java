package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration test that tests the REST calls made by {@link AirlineRestClient}
 */
@TestMethodOrder(MethodName.class)
class AirlineRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AirlineRestClient newAirlineRestClient() {
    int port = Integer.parseInt(PORT);
    return new AirlineRestClient(HOSTNAME, port);
  }

  /**
   * Integration test that checks that removeAllAirlines method works properly
   */
  @Test
  void test0RemoveAllAirlines() throws IOException {
    AirlineRestClient client = newAirlineRestClient();
    client.removeAllAirlines();
  }

  /**
   * Integration test that checks that addFlight properly adds a flight
   */
  @Test
  void test2AddFlight() throws IOException, ParserException {
    AirlineRestClient client = newAirlineRestClient();
    String airlineName = "AIRLINE";
    String flightNumber = "123";
    String src = "PDX";
    String srcDateTime = "8/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "8/06/2026 12:00 AM";

    client.addFlight(airlineName, flightNumber, src, srcDateTime, dest, destDateTime);

    Airline airline = client.getAirline(airlineName);
    assertThat(airline.getName(), equalTo(airlineName));
    assertThat(airline.getFlights().size(), equalTo(1));
    assertThat(airline.getFlights().iterator().next().getNumber(), equalTo(Integer.parseInt(flightNumber)));
  }

  /**
   * Integration test that checks that an empty airline name throws an exception
   */
  @Test
  void test3EmptyAirlineNameThrowsException() {
    AirlineRestClient client = newAirlineRestClient();
    String emptyString = "";

    HttpRequestHelper.RestException ex =
      assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlight(emptyString, emptyString));
    assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    assertThat(ex.getMessage(), containsString(Messages.missingRequiredParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)));
  }}
