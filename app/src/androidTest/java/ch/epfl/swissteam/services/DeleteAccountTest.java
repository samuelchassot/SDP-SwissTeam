package ch.epfl.swissteam.services;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class DeleteAccountTest extends FirebaseTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        super.initialize();
        User testUser = TestUtils.getTestUser();
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(testUser.getGoogleId_());


    }

    @Override
    public void terminate() {
        super.terminate();
    }

    @Test
    public void deleteCurrentUser(){
        int x =1+1;
    }
}
