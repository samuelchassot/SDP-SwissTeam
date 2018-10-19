package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class ProfileDisplayFragmentTest extends FirebaseTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void openFragment() {
        User testUser = TestUtils.getATestUser();
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(testUser.getGoogleId_());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_profile));

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        assertThat(((TextView)mainActivityRule_.getActivity().findViewById(R.id.textview_profiledisplay_name)).getText().toString(), is(testUser.getName_()));
        assertThat(((TextView)mainActivityRule_.getActivity().findViewById(R.id.textview_profiledisplay_description)).getText().toString(),
                is(testUser.getDescription_()));
                */

        onView(withId(R.id.button_profiledisplay_modify)).perform(click());
        
    }
}
