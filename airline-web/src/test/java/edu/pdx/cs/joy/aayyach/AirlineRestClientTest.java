package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;
import edu.pdx.cs.joy.web.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

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

  @Test
  void getAirlinePerformsHttpGetWithAirlineNameParameter() throws ParserException, IOException {
    String airlineName = "Airline";
    Airline airline = new Airline(airlineName);
    int flightNumber = 123;
    airline.addFlight(new Flight(flightNumber));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName)))).thenReturn(airlineAsText(airline));
    
    AirlineRestClient client = new AirlineRestClient(http);

    Airline fetched = client.getAirline(airlineName);
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
