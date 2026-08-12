package edu.pdx.cs.joy.aayyach;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import edu.pdx.cs.joy.ParserException;

public class CreateFlightActivity extends AppCompatActivity {

    private final List<Airline> airlines = new ArrayList<>();
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_flight);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = findViewById(R.id.spinner2);
        loadAirlineData();
        displayAirlineOptions();
    }

    public void backToHome(View view) {
        finish();
    }

    public void displayAirlineOptions() {
        ArrayList<String> names = new ArrayList<>();

        for (Airline airline : airlines) { names.add(airline.getName()); }
        if (names.isEmpty()) {
            Toast.makeText(this, "There are no airlines to add a flight to", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadAirlineData() {
        String [] airlineFiles = fileList();
        if (airlineFiles == null) { return; }

        for (String file : airlineFiles) {
            if (file.startsWith("airlines_") && file.endsWith(".txt")) {
                try (InputStreamReader isr = new InputStreamReader(openFileInput(file))) {
                    TextParser parser = new TextParser(isr);
                    Airline readAirline = parser.parse();
                    if (readAirline != null) { airlines.add(readAirline); }
                } catch (ParserException | IOException e) {
                    Toast.makeText(this, "Could not load airline data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void createFlight(View view) {
        if (spinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select an airline first", Toast.LENGTH_LONG).show();
            return;
        }

        String selected = spinner.getSelectedItem().toString();
        for ( Airline airline : airlines) {
            if (airline.getName().equals(selected)) {
                EditText flightNumInput = findViewById(R.id.flightNumber);
                String flightNumber = flightNumInput.getText().toString();
                if (!flightNumber.matches("^\\d+") || flightNumber.isEmpty()) {
                    Toast.makeText(this, "Flight number must not be empty and must be numerical", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText srcInput = findViewById(R.id.src);
                String src = srcInput.getText().toString();
                if (!src.matches("[a-zA-Z]{3}") || src.isEmpty()) {
                    Toast.makeText(this, "Source airport must not be empty and must be 3 alphabetical chars", Toast.LENGTH_LONG).show();
                    return;
                }

                LocalDateTime srcDT;
                EditText srcDateTimeInput = findViewById(R.id.srcDateTime);
                String srcDateTime = srcDateTimeInput.getText().toString();
                if (srcDateTime.isEmpty()) {
                    Toast.makeText(this, "Source date/time cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
                    srcDT = LocalDateTime.parse(srcDateTime, formatter);
                } catch (DateTimeParseException e) {
                    Toast.makeText(this, "Source Date/Time must be in format m/d/yyyy h:mm am/pm", Toast.LENGTH_LONG).show();
                    return;
                }

                EditText destInput = findViewById(R.id.dest);
                String dest = destInput.getText().toString();
                if (!dest.matches("[a-zA-Z]{3}") || dest.isEmpty()) {
                    Toast.makeText(this, "Destination airport must not be empty and must be 3 alphabetical chars", Toast.LENGTH_LONG).show();
                    return;
                }

                LocalDateTime destDT;
                EditText destDateTimeInput = findViewById(R.id.destDateTime);
                String destDateTime = destDateTimeInput.getText().toString();
                if (destDateTime.isEmpty()) {
                    Toast.makeText(this, "Destination date/time cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
                    destDT = LocalDateTime.parse(destDateTime, formatter);
                } catch (DateTimeParseException e) {
                    Toast.makeText(this, "Destination Date/Time must be in format m/d/yyyy h:mm am/pm", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    Flight flight = new Flight(Integer.parseInt(flightNumber), src, srcDT, dest, destDT);
                    airline.addFlight(flight);
                    writeAirlineToFile(airline);
                    Toast.makeText(this, "Flight " + flightNumber + " has been created for " + selected, Toast.LENGTH_LONG).show();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Error saving flight: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void writeAirlineToFile(Airline airline) throws IOException {
        File dataDir = this.getFilesDir();
        File airlinesFile = new File(dataDir, "airlines_" + airline.getName() + ".txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(airlinesFile))) {
            TextDumper dumper = new TextDumper(pw);
            dumper.dump(airline);
            pw.flush();
        }
    }
}