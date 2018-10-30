package ch.epfl.swissteam.services;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.sleep;

@RunWith(AndroidJUnit4.class)
public class MyPostFragmentTest extends FirebaseTest{
    private Post post;
    private String id;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Override
    public void initialize(){
        LocationManager.get().setMock();
        DBUtility.get().setUser(TestUtils.getATestUser());
        id = "1234";
        GoogleSignInSingleton.putUniqueID(id);
        post = new Post("1234_1539704399119", "Title", "1234", "Body", 1539704399119L,  10, 20);
        DBUtility.get().setPost(post);
        sleep(400);
    }

    @Test
    public void canOpenMyPostsFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
    }

    @Test
    public void canSwipeLeft(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
        sleep(200);
        onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));

    }

    @Test
    public void canDelete(){

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
        sleep(200);
        onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));
        onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.button_mypostadapter_delete)));
    }

    @Test
    public void canEdit(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_myposts));
        sleep(200);
        onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));
        onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.button_mypostadapter_edit)));

        onView(withId(R.id.edittext_mypostedit_title)).check(matches(withText("Title")));
        onView(withId(R.id.edittext_mypostedit_body)).check(matches(withText("Body")));
        onView(withId(R.id.edittext_mypostedit_title)).perform(typeText(" from unit test")).perform(closeSoftKeyboard());
        onView(withId(R.id.edittext_mypostedit_body)).perform(typeText(" from unit test")).perform(closeSoftKeyboard());
        onView(withId(R.id.action_save)).perform(click());

        onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));
        onView(withId(R.id.recyclerview_mypostsfragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.button_mypostadapter_edit)));

        onView(withId(R.id.edittext_mypostedit_title)).check(matches(withText("Title from unit test")));
        onView(withId(R.id.edittext_mypostedit_body)).check(matches(withText("Body from unit test")));
        onView(withId(R.id.edittext_mypostedit_title)).perform(clearText()).perform(typeText("Title")).perform(closeSoftKeyboard());
        onView(withId(R.id.edittext_mypostedit_body)).perform(clearText()).perform(typeText("Body")).perform(closeSoftKeyboard());
        onView(withId(R.id.action_save)).perform(click());
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
