package edu.pdx.cs.joy.aayyach;

import edu.pdx.cs.joy.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  public Airline parse() throws ParserException {
    Airline airline = null;
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        if (airline == null) {
          airline = new Airline(line);
        } else {
          airline.addFlight(new Flight(Integer.parseInt(line)));
        }
      }

    } catch (IOException e) {
      throw new ParserException("While parsing airline", e);
    }

    return airline;
  }
}
