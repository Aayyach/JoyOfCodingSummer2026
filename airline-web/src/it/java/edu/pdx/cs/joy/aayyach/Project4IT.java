package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.InvokeMainTestCase;
import edu.pdx.cs.joy.UncaughtExceptionInMain;
import edu.pdx.cs.joy.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    void test0RemoveAllMappings() throws IOException {
      AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
      client.removeAllAirlines();
    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getTextWrittenToStandardError(), containsString(Project4.MISSING_ARGS));
    }

    @Test
    void test2MissingAirlineName() {
        MainMethodResult result = invokeMain( Project4.class, HOSTNAME, PORT );

        assertThat(result.getTextWrittenToStandardError(), containsString("Missing airline name"));
    }

    @Test
    void test3NonExistentSourceAirlinePrintsErrorMessage() {
        String airlineName = "AIRLINE NAME";
        String flightNumber = "123";
        MainMethodResult result = invokeMain(Project4.class, HOSTNAME, PORT, airlineName, flightNumber);
        assertThat(result.getTextWrittenToStandardError(), containsString("** Missing source airport"));
    }

    @Test
    void test4AddFlight() {
        String airlineName = "AIRLINE NAME";
        String flightNumber = "123";
        String source = "PDX";
        String departDate = "8/5/2026";
        String departTime = "9:00";
        String departExt =  "PM";
        String dest = "OAK";
        String arriveDate = "8/5/2026";
        String arriveTime = "11:00";
        String arriveExt = "PM";

        MainMethodResult result = invokeMain( Project4.class, HOSTNAME, PORT, airlineName, flightNumber, source, departDate, departTime, departExt, dest, arriveDate, arriveTime, arriveExt );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(Messages.definedAirlineNameAs(airlineName, flightNumber)));

        result = invokeMain( Project4.class, HOSTNAME, PORT, airlineName, flightNumber, source, departDate, departTime, departExt, dest, arriveDate, arriveTime, arriveExt );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        // assertThat(out, out, containsString(PrettyPrinter.formatDictionaryEntry(airlineName, flightNumber)));
        assertThat(out, containsString("Defined AIRLINE NAME as 123"));
    }
}