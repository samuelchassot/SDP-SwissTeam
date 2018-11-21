package ch.epfl.swissteam.services;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Class designed to wrap tests to prevent actual interaction with the Firebase DB or the Location Manager.
 * Subclasses have to define a constructor where they call setTestRule with argument being C.class
 *
 * @param <C> Activity to start the test from (Not the one being tested!)
 *
 * @author Adrian Baudat
 */
@RunWith(AndroidJUnit4.class)
public abstract class SocializeTest<C extends Activity> {

    @Rule
    public IntentsTestRule<C> testRule_;

    public void setTestRule(Class<C> className) {
        testRule_ = new IntentsTestRule<>(className, true, false);
    }

    @Before
    public final void setUp() {
        DBUtility.get().getInstance().goOffline();
        LocationManager.get().setMock();
        initialize();
        testRule_.launchActivity(getActivityIntent());
        initializeView();
    }

    @After
    public final void tearDown() {
        terminate();
        DBUtility.get().getInstance().purgeOutstandingWrites();
        DBUtility.get().getInstance().goOnline();
        LocationManager.get().unsetMock();
    }

    /**
     * Use this method to initialize the view (navigate to fragment, do modifications on views, ...)
     */
    public void initializeView(){

    }

    /**
     * Use this method to initialize the DB/Location before your tests.
     */
    public void initialize() {

    }

    /**
     * Use this method to terminate your tests correctly.
     */
    public void terminate() {

    }

    /**
     * Use this method to set the intent used to start the activity.
     *
     * @return By default, returns an empty intent.
     */
    public Intent getActivityIntent() {
        return new Intent();
    }
}
