package ch.epfl.swissteam.services;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class CreatePostFragmentTest extends FirebaseTest{

    private static String title = "Searching for someone to mow my lawn before Friday!",
    body = "I need someone fast please!!",
    longBody = "And they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming.";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        GoogleSignInSingleton.putUniqueID(TestUtils.M_GOOGLE_ID);
    }

    @Test
    public void canOpenCreatePostFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
        onView(withId(R.id.fragment_my_posts)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(typeText(title)).check(matches(withText(title)));
        onView(withId(R.id.plaintext_createpostfragment_body)).perform(typeText(body)).check(matches(withText(body)));
    }

    @Test
    public void cantSendWithoutBody() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
        onView(withId(R.id.fragment_my_posts)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(typeText(title), ViewActions.closeSoftKeyboard()).check(matches(withText(title)));
        onView(withId(R.id.button_createpostfragment_send)).perform(click());
        onView(withText(R.string.createpostfragment_bodyempty)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    @Test
    public void canClickButtonWithLongBody() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
        onView(withId(R.id.fragment_my_posts)).perform(click());
        onView(withId(R.id.plaintext_createpostfragment_title)).perform(replaceText(longBody)).check(matches(withText(longBody)));
        onView(withId(R.id.plaintext_createpostfragment_body)).perform(replaceText(longBody), ViewActions.closeSoftKeyboard()).check(matches(withText(longBody)));
        onView(withId(R.id.button_createpostfragment_send)).perform(click());
    }
}
