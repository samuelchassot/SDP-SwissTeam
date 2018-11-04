package ch.epfl.swissteam.services;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;

public class DeleteAccountTest extends FirebaseTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);
    
    @Override
    public void initialize() {
        super.initialize();
        User testUser = TestUtils.getTestUser();
        testUser.addToDB(DBUtility.get().getDb_());

    }

    @Override
    public void terminate() {
        super.terminate();
    }
}
