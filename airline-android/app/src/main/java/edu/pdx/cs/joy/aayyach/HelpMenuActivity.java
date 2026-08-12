
package edu.pdx.cs.joy.aayyach;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HelpMenuActivity extends AppCompatActivity {

    protected TextView readMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        readMeContent();
    }

    public void backToHome(View view) {
        finish();
    }

    public void readMeContent() {
        readMe = findViewById(R.id.textView2);
        readMe.setText("Developer: \n" +
                "Alaa Ayyach\n" +
                "\n" +
                "Assignment:\n" +
                "Project 5/5 | Designing an Airline Application | CS410P Joy of Coding Java and Android at PSU\n" +
                "\n" +
                "Description:\n" +
                "This is Project 5 of 5 assigned in the Joy of Coding Java and Android at Portland State University. " +
                "It consists of taking what was assigned through projects 1, 2, and 3 and using the code to create " +
                "a fully functional android application. Out of all of the assignments that have been assigned thus far, " +
                "I will say this has been one of the most enjoyable ones to complete. To successfully complete the " +
                "assignment, you must make sure that you allow a user all of the functionality of the previous projects " +
                "and provide a pleasant user interface. I learned a lot while completing this assignment, and it reminded " +
                "me a lot of full-stack development. \n" +
                "\n" +
                "For example, the design studio and XML really reminded me of HTML and CSS, and I would say they're pretty " +
                "similar in terms of their attributes. When I first started this assignment, it felt daunting and intimidating " +
                "but after completing it, I can say that I enjoyed creating the app from start to finish. I think that I could " +
                "have done a much better job on the UI, but I decided to keep it simple and just go for functionality without " +
                "sacrificing a pleasant user interface. Overall, I learned a lot, and while I would have done a couple things " +
                "differently, I'm very happy with the results. I also enjoyed working with the internal storage to figure out " +
                "to store Airline data and have the data persist even after restarting the app. This was a great learning " +
                " opportunity and heavily aligned with my interests in full-stack web-development.");
    }


}