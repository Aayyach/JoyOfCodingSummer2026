package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperParserTest {

  private Airline dumpAndParse(Airline airline) throws ParserException {
    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);

    String text = sw.toString();

    TextParser parser = new TextParser(new StringReader(text));
    return parser.parse();
  }

  @Test
  void dumpedTextCanBeParsed() throws ParserException {
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

    Airline read = dumpAndParse(airline);
    assertThat(read.getName(), equalTo(airlineName));
    assertThat(read.getFlights().size(), equalTo(1));
    assertThat(read.getFlights().iterator().next().getNumber(), equalTo(flightNumber));
  }
}
