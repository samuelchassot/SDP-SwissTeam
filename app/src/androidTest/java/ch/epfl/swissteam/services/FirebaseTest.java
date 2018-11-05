package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Class designed to wrap tests that interact with the Firebase Realtime Database to prevent actual interaction with the DB.
 *
 * @author Adrian Baudat
 */
@RunWith(AndroidJUnit4.class)
public abstract class FirebaseTest {

    @Before
    public final void setUp() {
        DBUtility.get();
        FirebaseDatabase.getInstance().goOffline();
        initialize();
    }

    @After
    public final void tearDown() {
        terminate();
        FirebaseDatabase.getInstance().purgeOutstandingWrites();
        FirebaseDatabase.getInstance().goOnline();
    }

    /**
     * Use this method to initialize the DB before your tests.
     */
    public void initialize() {

    }

    /**
     * Use this method to terminate correctly your tests.
     */
    public void terminate() {

    }
}
