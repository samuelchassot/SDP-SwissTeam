package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.sleep;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;


@RunWith(AndroidJUnit4.class)
public class ServicesFragmentTest extends SocializeTest<MainActivity>{

    public ServicesFragmentTest(){
        setTestRule(MainActivity.class);
    }

    @Before
    public void initialize() {
        LocationManager.get().setMock();
        User testUser = TestUtils.getTestUser();
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(TestUtils.M_GOOGLE_ID);
    }

    @After
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Test
    public void openFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
        onView(withId(R.id.services_spinner)).perform(scrollTo()).perform(click());
    }

    @Test
    public void clickSearchWithoutKeyWords(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
        sleep(1000);
        onView(withId(R.id.button_services_search)).perform(click());
    }

    @Test
    public void clickSearchWithKeywords(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
        sleep(1000);
        onView(withId(R.id.edittext_services_keywordsinput)).perform(clearText()).perform(typeText("Python"));
        closeSoftKeyboard();
        sleep(1000);
        onView(withId(R.id.button_services_search)).perform(click());
        sleep(1000);
        onView(withId(R.id.edittext_services_keywordsinput)).perform(clearText()).perform(typeText("Java"));
        closeSoftKeyboard();
        sleep(1000);
        onView(withId(R.id.button_services_search)).perform(click());
        sleep(1000);
        onView(withId(R.id.edittext_services_keywordsinput)).perform(clearText()).perform(typeText("NoUserWillHaveThisKeywords"));
        closeSoftKeyboard();
        sleep(1000);
        onView(withId(R.id.button_services_search)).perform(click());
    }

    @Test
    public void clickSearchCloseKeyBoard(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_services));
        sleep(1000);
        onView(withId(R.id.edittext_services_keywordsinput)).perform(clearText()).perform(typeText("Python"));
        sleep(1000);
        onView(withId(R.id.button_services_search)).perform(scrollTo()).perform(click());
        sleep(1000);
        onView(withId(R.id.edittext_services_keywordsinput)).perform(clearText()).perform(typeText("Java"));
        closeSoftKeyboard();
        sleep(1000);
        onView(withId(R.id.button_services_search)).perform(scrollTo()).perform(click());
        sleep(1000);
    }
}
