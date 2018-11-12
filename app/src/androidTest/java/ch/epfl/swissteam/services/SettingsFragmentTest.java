package ch.epfl.swissteam.services;

import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Checkable;
import android.widget.SeekBar;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {
    private SettingsDbHelper helper;
    private String id;
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize() {
        LocationManager.get().setMock();
        id = "1234";
        helper = new SettingsDbHelper(mActivityRule.getActivity().getApplicationContext());
        SettingsDBUtility.addRowIfNeeded(helper, id);
    }

    @After
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Test
    public void canOpenSettingsFragment() {
        TestUtils.addRowToLocalSettingsDB(mActivityRule.getActivity(), TestUtils.M_GOOGLE_ID);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));
    }

    @Test
    public void displayRadius(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));

        String s = String.format(Locale.ENGLISH,
                mActivityRule.getActivity().getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
                SettingsDBUtility.retrieveRadius(helper, id)/1000.0);
        onView(withId(R.id.textview_settings_currentradius)).check(matches(withText(s)));
    }

    @Test
    public void canModifyRadius() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));

        onView(withId(R.id.seekbar_settings_radius)).perform(scrollTo()).perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_LEFT, Press.FINGER));

        String s = String.format(Locale.ENGLISH,
                mActivityRule.getActivity().getResources().getString(R.string.settings_seekbar_currentradius) + " %.2f km",
                0/1000.0);
        onView(withId(R.id.textview_settings_currentradius)).check(matches(withText(s)));
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
