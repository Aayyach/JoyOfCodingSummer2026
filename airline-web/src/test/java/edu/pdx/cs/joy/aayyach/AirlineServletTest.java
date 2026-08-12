package edu.pdx.cs.joy.aayyach;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
class AirlineServletTest {

  /**
   * Adds one airline name to server
   */
  @Test
  void addOneAirlineNameToTheWebServer() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "AIRLINE";
    String flightNumber = "123";
    String src = "PDX";
    String srcDateTime = "8/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "8/06/2026 12:00 AM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.SRC_AIRPORT_PARAMETER)).thenReturn(src);
    when(request.getParameter(AirlineServlet.DEPART_TIME_PARAMETER)).thenReturn(srcDateTime);
    when(request.getParameter(AirlineServlet.DEST_AIRPORT_PARAMETER)).thenReturn(dest);
    when(request.getParameter(AirlineServlet.ARRIVAL_TIME_PARAMETER)).thenReturn(destDateTime);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    assertThat(stringWriter.toString(), containsString(Messages.definedAirlineNameAs(airlineName, flightNumber)));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
    assertThat(servlet.getAirline(airlineName).getName(), equalTo("AIRLINE"));
  }

  /**
   * Unit test to check that a missing airline name returns a 404 HTTP error code
   */
  @Test
  void missingAirlineNameParameterReturnsPreconditionFailedStatusCode() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(null);

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    // Nothing is written to the response's PrintWriter
    String message = Messages.missingRequiredParameter(AirlineServlet.AIRLINE_NAME_PARAMETER);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);

  }

  /**
   * Unit test to check that the filter method is working correctly 
   */
  @Test
  void filterMethodReturnsTheCorrectFlights() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "AIRLINE";
    String flightNumber = "123";
    String src = "PDX";
    String srcDateTime = "8/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "8/06/2026 12:00 AM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.SRC_AIRPORT_PARAMETER)).thenReturn(src);
    when(request.getParameter(AirlineServlet.DEPART_TIME_PARAMETER)).thenReturn(srcDateTime);
    when(request.getParameter(AirlineServlet.DEST_AIRPORT_PARAMETER)).thenReturn(dest);
    when(request.getParameter(AirlineServlet.ARRIVAL_TIME_PARAMETER)).thenReturn(destDateTime);

    HttpServletResponse response = mock(HttpServletResponse.class);
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    Airline returned = servlet.filter(airlineName, src, dest);
    ArrayList<Flight> flights = new ArrayList<>(returned.getFlights());
    assertThat(flights.size(), equalTo(1));
    assertThat(flights.get(0).getSource(), equalTo(src));
    assertThat(flights.get(0).getDestination(), equalTo(dest));
  }

  /**
   * Unit test to check that the filter method is working correctly
   */
  @Test
  void filterMethodReturnsNullIfNoAirlineExists() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "AIRLINE";
    String src = "PDX";
    String dest = "OAK";

    Airline returned = servlet.filter(airlineName, src, dest);
    assertThat(returned, equalTo(null));
  }

  /**
   * Unit test to check that the writeAirline method is working correctly
   */
  @Test
  void writeAirlineWritesTheAirlineNameToHTTPResponse() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "AIRLINE";
    String flightNumber = "123";
    String src = "PDX";
    String srcDateTime = "8/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "8/06/2026 12:00 AM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.SRC_AIRPORT_PARAMETER)).thenReturn(src);
    when(request.getParameter(AirlineServlet.DEPART_TIME_PARAMETER)).thenReturn(srcDateTime);
    when(request.getParameter(AirlineServlet.DEST_AIRPORT_PARAMETER)).thenReturn(dest);
    when(request.getParameter(AirlineServlet.ARRIVAL_TIME_PARAMETER)).thenReturn(destDateTime);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    assertThat(stringWriter.toString(), containsString(Messages.definedAirlineNameAs(airlineName, flightNumber)));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
    assertThat(servlet.getAirline(airlineName).getName(), equalTo("AIRLINE"));

    HttpServletResponse response2 = mock(HttpServletResponse.class);

    StringWriter stringWriter2 = new StringWriter();
    PrintWriter pw2 = new PrintWriter(stringWriter2, true);

    when(response2.getWriter()).thenReturn(pw2);

    servlet.writeAirline(airlineName, response2);
    verify(response2).setStatus(statusCode.capture());
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
  }

  /**
   * Unit test to check that writeAirline returns null if no airline exists
   */
  @Test
  void writeAirlineWritesReturns404StatusCodeWithNoValidAirline() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    String airlineName = "AIRLINE";

    HttpServletResponse response = mock(HttpServletResponse.class);
    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.writeAirline(airlineName, response);

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_NOT_FOUND));
  }

  /**
   * Unit test to check that the writeFilteredAirline method is working correctly
   */
  @Test
  void writeFilteredAirlineWritesTheAirlineNameToHTTPResponse() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "AIRLINE";
    String flightNumber = "123";
    String src = "PDX";
    String srcDateTime = "8/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "8/06/2026 12:00 AM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.SRC_AIRPORT_PARAMETER)).thenReturn(src);
    when(request.getParameter(AirlineServlet.DEPART_TIME_PARAMETER)).thenReturn(srcDateTime);
    when(request.getParameter(AirlineServlet.DEST_AIRPORT_PARAMETER)).thenReturn(dest);
    when(request.getParameter(AirlineServlet.ARRIVAL_TIME_PARAMETER)).thenReturn(destDateTime);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    assertThat(stringWriter.toString(), containsString(Messages.definedAirlineNameAs(airlineName, flightNumber)));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
    assertThat(servlet.getAirline(airlineName).getName(), equalTo("AIRLINE"));

    HttpServletResponse response2 = mock(HttpServletResponse.class);

    StringWriter stringWriter2 = new StringWriter();
    PrintWriter pw2 = new PrintWriter(stringWriter2, true);

    when(response2.getWriter()).thenReturn(pw2);

    servlet.writeFilteredAirline(airlineName, src, dest, response2);
    verify(response2).setStatus(statusCode.capture());
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
  }

  /**
   * Unit test to check that writeFilteredAirline returns null if no airline exists
   */
  @Test
  void writeFilteredAirlineWritesReturns404StatusCodeWithNoValidAirline() throws IOException {
    AirlineServlet servlet = new AirlineServlet();
    String airlineName = "AIRLINE";

    HttpServletResponse response = mock(HttpServletResponse.class);
    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.writeFilteredAirline(airlineName, "PDX", "OAK", response);

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_NOT_FOUND));
  }

  @Test
  void doDeleteDeletesAllAirlineEntries() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "AIRLINE";
    String flightNumber = "123";
    String src = "PDX";
    String srcDateTime = "8/05/2026 10:00 PM";
    String dest = "OAK";
    String destDateTime = "8/06/2026 12:00 AM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumber);
    when(request.getParameter(AirlineServlet.SRC_AIRPORT_PARAMETER)).thenReturn(src);
    when(request.getParameter(AirlineServlet.DEPART_TIME_PARAMETER)).thenReturn(srcDateTime);
    when(request.getParameter(AirlineServlet.DEST_AIRPORT_PARAMETER)).thenReturn(dest);
    when(request.getParameter(AirlineServlet.ARRIVAL_TIME_PARAMETER)).thenReturn(destDateTime);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    HttpServletRequest request2 = mock(HttpServletRequest.class);
    HttpServletResponse response2 = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    StringWriter stringWriter2 = new StringWriter();
    PrintWriter pw2 = new PrintWriter(stringWriter, true);

    when(response2.getWriter()).thenReturn(pw2);

    servlet.doDelete(request2, response2); 

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response2).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
  }

  /**
   * Null parameter returns HTTP error status code
   */
  @Test
  void nullParameterNameReturnsHTTPErrorStatusCode() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletResponse response = mock(HttpServletResponse.class);
    String param = null;

    servlet.missingRequiredParameter(response, param);
    verify(response).sendError(HttpServletResponse.SC_PRECONDITION_FAILED, Messages.missingRequiredParameter(param));
  }
  
}
