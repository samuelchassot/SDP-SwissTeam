package ch.epfl.swissteam.services;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GoogleSignInSingletonTest extends SocializeTest<MainActivity> {

    private User testUser = TestUtils.getTestUser();

    public GoogleSignInSingletonTest(){
        setTestRule(MainActivity.class);
    }

    @Override
    public void initialize() {
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(testUser.getGoogleId_());
    }

    @Test
    public void getUserID(){
        assertEquals(GoogleSignInSingleton.get().getClientUniqueID(), testUser.getGoogleId_());
    }
}
