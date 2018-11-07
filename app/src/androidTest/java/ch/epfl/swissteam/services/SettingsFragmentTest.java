package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Checkable;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initialize() {
        LocationManager.get().setMock();
        TestUtils.addRowToLocalSettingsDB(mActivityRule.getActivity().getApplicationContext(), TestUtils.M_GOOGLE_ID);
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
    public void canModifyRadius() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));

        //TODO
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
