package edu.pdx.cs.joy.aayyach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.pdx.cs.joy.ParserException;

public class CreateAirlineActivity extends AppCompatActivity {

    public static final String AIRLINE_NAME = "AIRLINE_NAME";
    private Airline airline;
    private final List<Airline> airlines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_airline);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadAirlineData();
    }

    public void createAirline(View view) {
        EditText name = findViewById(R.id.airlineName);
        String airlineName = name.getText().toString();

        if (airlineName.isEmpty()) {
            Toast.makeText(this, "Airline name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        this.airline = new Airline(airlineName);

        // Write airline to internal storage
        try {
            writeAirlinesToFile();
            if (this.airline.getName().equals(airlineName)) {
                Toast.makeText(this, "Airline " + airlineName + " has been created", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "While writing Airline to file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void writeAirlinesToFile() throws IOException {
        if (this.airline == null) { return; }

        File airlinesFile = getAirlinesFile();
        try (PrintWriter pw = new PrintWriter(new FileWriter(airlinesFile))) {
            if (this.airline != null) {
                TextDumper dumper = new TextDumper(pw);
                dumper.dump(this.airline);
            }
            pw.flush();
        }
    }

    private File getAirlinesFile() {
        File dataDir = this.getFilesDir();
        return new File(dataDir, "airlines_" + this.airline.getName() + ".txt");
    }

    public void backToHome(View view) {
        EditText airlineNameInput = findViewById(R.id.airlineName);
        String airlineName = airlineNameInput.getText().toString();

        if (this.airline != null) {
            Intent intent = new Intent();
            intent.putExtra(AIRLINE_NAME, airlineName);
            setResult(RESULT_OK, intent);
        }
        finish();
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

    public void openFlightScreen(View view) {
        Intent intent = new Intent(this, CreateFlightActivity.class);
        startActivity(intent);
    }

}