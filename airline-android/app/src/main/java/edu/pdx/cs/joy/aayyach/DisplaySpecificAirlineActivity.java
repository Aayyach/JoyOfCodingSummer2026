package edu.pdx.cs.joy.aayyach;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.pdx.cs.joy.ParserException;

public class DisplaySpecificAirlineActivity extends AppCompatActivity {

    private final List<Airline> airlines = new ArrayList<>();
    protected TextView airlineInfo;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_specific_airline);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spinner = findViewById(R.id.airlines);
        airlineInfo = findViewById(R.id.textView3);
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
            Toast.makeText(this, "There are no airlines to display", Toast.LENGTH_LONG).show();
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

    public void filter(View view) {
        if (spinner.getSelectedItem() == null) {
            return;
        }
        EditText srcInput = findViewById(R.id.srcSearch);
        String src = srcInput.getText().toString();
        if (!src.matches("[a-zA-Z]{3}") || src.isEmpty()) {
            Toast.makeText(this, "Source airport must not be empty and must be 3 alphabetical chars", Toast.LENGTH_LONG).show();
            return;
        }

        EditText destInput = findViewById(R.id.destSearch);
        String dest = destInput.getText().toString();
        if (!dest.matches("[a-zA-Z]{3}") || dest.isEmpty()) {
            Toast.makeText(this, "Destination airport must not be empty and must be 3 alphabetical chars", Toast.LENGTH_LONG).show();
            return;
        }

        String selected = spinner.getSelectedItem().toString();
        Airline rightAirline = null;
        for (Airline airline : airlines) {
            if (airline.getName().equals(selected)) {
                rightAirline = airline;
                break;
            }
        }

        if (rightAirline == null) {
            return;
        }

        Airline filtered = new Airline(rightAirline.getName());
        Collection<Flight> flights = rightAirline.getFlights();
        for (Flight flight : flights) {
            if (flight.getSource().equalsIgnoreCase(src) && flight.getDestination().equalsIgnoreCase(dest)) {
                filtered.addFlight(flight);
            }
        }

        PrettyPrinter pw = new PrettyPrinter(findViewById(R.id.textView3));
        pw.dump(filtered);
    }
}