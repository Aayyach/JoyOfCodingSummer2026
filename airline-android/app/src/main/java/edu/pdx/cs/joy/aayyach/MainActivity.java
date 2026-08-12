package edu.pdx.cs.joy.aayyach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import edu.pdx.cs.joy.ParserException;

public class MainActivity extends AppCompatActivity {

    private static final int CREATE_AIRLINE_REQUEST = 1;
    private final HashMap<String, Airline> airlines = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadAirlineData();
    }

    public void openAirlineScreen(View view) {
        Intent intent = new Intent(this, CreateAirlineActivity.class);
        startActivityForResult(intent, CREATE_AIRLINE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_AIRLINE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String airlineName = data.getStringExtra(CreateAirlineActivity.AIRLINE_NAME);

                    if (airlineName != null) {
                        loadAirlineData();
                    }
                }
            }
        }
    }

    private void loadAirlineData() {
        String [] airlineFiles = fileList();
        if (airlineFiles == null) { return; }

        for (String file : airlineFiles) {
            if (file.startsWith("airline_") && file.endsWith(".txt")) {
                try (FileReader fr = new FileReader(file)) {
//                    TextParser parser = new TextParser(new FileReader("airlines.txt"));
                    TextParser parser = new TextParser(fr);

                    Airline readAirline = parser.parse();
                    if (readAirline != null) { airlines.put(readAirline.getName(), readAirline); }
                } catch (ParserException | IOException e) {
                    Toast.makeText(this, "Could not load airline data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}