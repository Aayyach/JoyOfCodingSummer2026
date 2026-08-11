package edu.pdx.cs.joy.aayyach;

import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.OutputStreamWriter; 
import static org.hamcrest.CoreMatchers.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrettyPrinterTest {
    
    /**
     * Unit test to check that the formatWordCount method works correctly
     */
    @Test
    void formatWordCountReturnsCorrectString() {
        StringWriter stringWriter = new StringWriter();
        PrettyPrinter pw = new PrettyPrinter(stringWriter);

        String returned = pw.formatWordCount(2);
        assertEquals(returned, "Airline on server contains 2 words");
    }

    /**
     * Unit test to check that the formatAirlineEntry method works correctly
     */
    @Test
    void formatAirlineEntryReturnsCorrectString() {
        StringWriter stringWriter = new StringWriter();
        PrettyPrinter pw = new PrettyPrinter(stringWriter);

        String returned = pw.formatAirlineEntry("Airline", "123");
        assertEquals(returned, "  Airline -> 123");
    }

    /**
     * Unit test to check that the dump method works correctly
     */
    @Test
    void dumpMethodWorksAsIntended() {
        Airline test = new Airline("Test");
        LocalDateTime source = LocalDateTime.of(2026, 7, 29, 8, 0);
        LocalDateTime dest = LocalDateTime.of(2026, 7, 29, 10, 0);

        Flight testFlight = new Flight(123, "PDX", source, "OAK", dest);
        test.addFlight(testFlight);

        StringWriter stringWriter = new StringWriter();
        PrettyPrinter pw = new PrettyPrinter(stringWriter);
        pw.dump(test);
        assertThat(stringWriter.toString(), containsString("Test with 1 flights"));
    }
    
}
