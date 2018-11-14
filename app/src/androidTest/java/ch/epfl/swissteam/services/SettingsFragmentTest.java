package ch.epfl.swissteam.services;

import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static ch.epfl.swissteam.services.TestUtils.sleep;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize() {
        LocationManager.get().setMock();
        SettingsDbHelper helper = new SettingsDbHelper(mActivityRule.getActivity().getApplicationContext());
        helper.getWritableDatabase().delete(SettingsContract.SettingsEntry.TABLE_NAME, null, null);
        SettingsDBUtility.addRowIfNeeded(helper, "1234");
        GoogleSignInSingleton.putUniqueID("1234");
    }

    @After
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Test
    public void canOpenSettingsFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));
    }

    @Test
    public void setHomeCanBePressed() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));
        sleep(100);
        closeSoftKeyboard();
        sleep(500);
        onView(withId(R.id.button_settings_sethome)).perform(scrollTo()).perform(click());
    }

/*
    @Test
    public void setHomeToCurrentLocationWorks() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));

        onView(withId(R.id.button_settings_sethome)).perform(click());
        double lat = ((SettingsFragment) mActivityRule.getActivity()).getHomeLat_();
        double lng = (SettingsFragment) mActivityRule.getActivity().getView()
        assertEquals().;
    }
*/
/*
    @Test
    public void isHomeMarkerMatchHomeLocation() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));

        onView(withId(R.id.button_settings_sethome)).perform(click());

    }
*/

    @Test
    public void displayRadius(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));

        //Default value for radius
        String s = String.format(Locale.ENGLISH,
                mActivityRule.getActivity().getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
                LocationManager.MAX_POST_DISTANCE/1000.0);
        onView(withId(R.id.textview_settings_currentradius)).check(matches(withText(s)));
    }

    @Test
    public void canModifyRadius() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));

        onView(withId(R.id.seekbar_settings_radius)).perform(scrollTo()).perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_LEFT, Press.FINGER));

        //sleep(1000);
        //String s = String.format(Locale.ENGLISH,
        //        mActivityRule.getActivity().getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
        //       0/1000.0);
        //onView(withId(R.id.textview_settings_currentradius)).perform(scrollTo()).check(matches(withText(s)));
    }

    @Test
    public void canSwitchDarkMode() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));

        //Check if dark mode not checked
        onView(withId(R.id.switch_settings_darkmode)).check(matches(isNotChecked()));

        //Click on dark mode and check if checked
        onView(withId(R.id.switch_settings_darkmode)).perform(click());
        onView(withId(R.id.switch_settings_darkmode)).check(matches(isChecked()));
    }
}
