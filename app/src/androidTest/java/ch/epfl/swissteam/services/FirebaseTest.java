package ch.epfl.swissteam.services;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
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
        FirebaseDatabase.getInstance().goOffline();
        initialize();
    }

    @After
    public final void tearDown() {
        FirebaseDatabase.getInstance().purgeOutstandingWrites();
        FirebaseDatabase.getInstance().goOnline();
    }

    /**
     * Use this method to initialize the DB before your tests.
     */
    public void initialize() {

    }
}
