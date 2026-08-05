package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

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
    airline.addFlight(new Flight(flightNumber));

    Airline read = dumpAndParse(airline);
    assertThat(read.getName(), equalTo(airlineName));
    assertThat(read.getFlights().size(), equalTo(1));
    assertThat(read.getFlights().iterator().next().getNumber(), equalTo(flightNumber));
  }
}
