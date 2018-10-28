package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.sleep;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class ProfileDisplayFragmentTest extends FirebaseTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void openFragmentAndChangeName() {
        User testUser = TestUtils.getTestUser();
//        DBUtility.get().setUser(testUser);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_profile));
        sleep(1000);
        onView(withId(R.id.textview_profiledisplay_name)).check(matches(withText(testUser.getName_())));
        onView(withId(R.id.textview_profiledisplay_description)).check(matches(withText(testUser.getDescription_())));
        sleep(1000);
        onView(withId(R.id.button_profiledisplay_modify)).perform(scrollTo()).perform(click());

    }

    @Test
    public void openDisplayFragmentThenOpenSettingsFragmentAndCancel(){

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_profile));
        sleep(1000);
        onView(withId(R.id.button_profiledisplay_modify)).perform(scrollTo()).perform(click());
        sleep(1000);
        onView(withId(R.id.button_profilesettings_cancel)).perform(scrollTo()).perform(click());
    }

    @Test
    public void changeNameSaveAndCheck(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_profile));
        sleep(1000);
        onView(withId(R.id.button_profiledisplay_modify)).perform(scrollTo()).perform(click());
        sleep(1000);
        String newName = "test New Name";
        onView(withId(R.id.edittext_profilesettings_name)).perform(clearText()).perform(typeText(newName)).perform(closeSoftKeyboard());
        sleep(1000);
        onView(withId(R.id.button_profilesettings_save)).perform(scrollTo()).perform(click());
        sleep(1000);
        onView(withId(R.id.textview_profiledisplay_name)).perform(scrollTo()).check(matches(withText(newName)));
        User testUser = TestUtils.getTestUser();
        testUser.addToDB(DBUtility.get().getDb_());
    }


    @Override
    public void initialize() {
        super.initialize();
        User testUser = TestUtils.getTestUser();
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(testUser.getGoogleId_());
    }
}
