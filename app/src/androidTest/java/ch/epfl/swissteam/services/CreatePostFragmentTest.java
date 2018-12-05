package ch.epfl.swissteam.services;

import android.Manifest;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.getTestUser;
import static ch.epfl.swissteam.services.TestUtils.personalClick;
import static ch.epfl.swissteam.services.TestUtils.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class CreatePostFragmentTest extends SocializeTest<MainActivity>{

    private static String title = "Searching for someone to mow my lawn before Friday!",
    body = "I need someone fast please!!",
    longBody = "And they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming.";

    public CreatePostFragmentTest(){
        setTestRule(MainActivity.class);
    }

    @Override
    public void initialize() {
        GoogleSignInSingleton.putUniqueID(TestUtils.getTestUser().getGoogleId_());
        getTestUser().addToDB(DBUtility.get().getDb_());
    }

    @Test
    public void canOpenCreatePostFragment() {
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(typeText(title));
        closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.plaintext_createpostfragment_title)).check(matches(withText(title)));
        onView(withId(R.id.plaintext_createpostfragment_body)).perform(typeText(body));
        closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.plaintext_createpostfragment_body)).check(matches(withText(body)));
    }

    @Test
    public void cantSendWithoutBody() {
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(typeText(title), ViewActions.closeSoftKeyboard()).check(matches(withText(title)));
        onView(withId(R.id.button_createpostfragment_send)).perform(click());
        onView(withText(R.string.createpostfragment_bodyempty)).inRoot(withDecorView(not(testRule_.getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void canClickButtonWithLongBody() {
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(replaceText(longBody)).check(matches(withText(longBody)));
        onView(withId(R.id.plaintext_createpostfragment_body)).perform(replaceText(longBody), ViewActions.closeSoftKeyboard()).check(matches(withText(longBody)));
        onView(withId(R.id.button_createpostfragment_send)).perform(click());
    }

    @Test
    public void canClickOnSliderSeveralTimes() {
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());
        onView(withId(R.id.switch_createpostfragment_location)).perform(click());
        onView(withId(R.id.switch_createpostfragment_location)).perform(click());
    }

    @Test
    public void isSliderTextCoherentWithSlider() {
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());

        onView(withId(R.id.textView_createpostfragment)).check(matches(withText(R.string.createpostfragment_location_switch_on)));

        onView(withId(R.id.switch_createpostfragment_location)).perform(click());
        onView(withId(R.id.textView_createpostfragment)).check(matches(withText(R.string.createpostfragment_location_switch_off)));
    }
    @Test
    public void createsPost1Week(){
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(typeText(title));
        closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.plaintext_createpostfragment_title)).check(matches(withText(title)));
        onView(withId(R.id.plaintext_createpostfragment_body)).perform(typeText(body));
        closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.spinner_createpost_timeout)).perform(click());
        sleep(100);
        onData(allOf(is(instanceOf(CreatePostFragment.TimeOut.class)), is(CreatePostFragment.TimeOut.oneWeek))).perform(click());
        sleep(100);
        onView(withId(R.id.button_createpostfragment_send)).perform(personalClick());

    }

    @Test
    public void createsPost1Month(){
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(typeText(title));
        closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.plaintext_createpostfragment_title)).check(matches(withText(title)));
        onView(withId(R.id.plaintext_createpostfragment_body)).perform(typeText(body));
        closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.spinner_createpost_timeout)).perform(click());
        sleep(100);
        onData(allOf(is(instanceOf(CreatePostFragment.TimeOut.class)), is(CreatePostFragment.TimeOut.threeDays))).perform(click());
        sleep(100);
        onView(withId(R.id.button_createpostfragment_send)).perform(personalClick());
    }

    @Test
    public void createsPost3Months(){
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(typeText(title));
        closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.plaintext_createpostfragment_title)).check(matches(withText(title)));
        onView(withId(R.id.plaintext_createpostfragment_body)).perform(typeText(body));
        closeSoftKeyboard();
        sleep(100);
        onView(withId(R.id.spinner_createpost_timeout)).perform(click());
        sleep(100);
        onData(allOf(is(instanceOf(CreatePostFragment.TimeOut.class)), is(CreatePostFragment.TimeOut.twoWeeks))).perform(click());
        sleep(100);
        onView(withId(R.id.button_createpostfragment_send)).perform(personalClick());
    }


    @Test
    public void isPostAtCurrentLocationWhenSliderOff() {
        openMyPostFragment();
        onView(withId(R.id.floatingbutton_addpost)).perform(click());

        onView(withId(R.id.switch_createpostfragment_location)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(replaceText(title));
        onView(withId(R.id.plaintext_createpostfragment_body)).perform(replaceText(body));
        closeSoftKeyboard();
        sleep(300);
        onView(withId(R.id.button_createpostfragment_send)).perform(personalClick());
        sleep(900);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_home));
        sleep(1000);
        onView(withId(R.id.action_refresh)).perform(click());
        sleep(1000);
        onView(withId(R.id.recyclerview_homefragment_posts)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        sleep(1000);
        onView(withId(R.id.textview_postactivity_distance)).check(matches(withText("0km away")));

    }

    private void openMyPostFragment(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
        sleep(500);
    }

}
