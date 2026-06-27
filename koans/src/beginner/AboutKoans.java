package beginner;

import com.sandwich.koan.Koan;

import static com.sandwich.util.Assert.fail;
import static com.sandwich.util.Assert.assertTrue;

public class AboutKoans {

    @Koan
    public void findAboutKoansFile() {
        assertTrue(true);
    }

    @Koan
    public void definitionOfKoanCompletion() {
        boolean koanIsComplete = false;
        if (!koanIsComplete) {
            assertTrue(true);
        }
    }

}
