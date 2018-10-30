package ch.epfl.swissteam.services;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GoogleSignInSingletonTest extends FirebaseTest {

    private User testUser = TestUtils.getTestUser();

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        super.initialize();
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(testUser.getGoogleId_());
    }

    @Test
    public void getUserID(){
        assertEquals(GoogleSignInSingleton.get().getClientUniqueID(), testUser.getGoogleId_());
    }


}
