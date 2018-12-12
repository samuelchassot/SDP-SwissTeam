package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import ch.epfl.swissteam.services.models.Post;
import ch.epfl.swissteam.services.models.User;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.providers.GoogleSignInSingleton;
import ch.epfl.swissteam.services.providers.TodoListDBUtility;
import ch.epfl.swissteam.services.utils.TodoListContract;
import ch.epfl.swissteam.services.utils.TodoListDbHelper;
import ch.epfl.swissteam.services.utils.Utils;
import ch.epfl.swissteam.services.view.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.sleep;
import static ch.epfl.swissteam.services.UtilsTest.clickChildViewWithId;
import static org.hamcrest.Matchers.not;

public class TodoListFragmentTest extends SocializeTest<MainActivity> {
    private Post postInBoth_;
    private Post postOnlyInLocalDB_;
    private User user_;

    public TodoListFragmentTest(){
        setTestRule(MainActivity.class);
    }

    @Override
    public void initialize() {
        user_ = TestUtils.getTestUser();
        postInBoth_ = TestUtils.getTestPost();

        long timestamp = (new Date()).getTime();
        String key = "5678" + "_" + timestamp;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = cal.getTime();
        postOnlyInLocalDB_ = new Post(key, "I'm not in Firebase", "5678",
                "But I'm in local DB", timestamp, 0, 0, Utils.dateToString(tomorrow));

        GoogleSignInSingleton.putUniqueID(user_.getGoogleId_());
        postInBoth_.addToDB(DBUtility.get().getDb_());
        user_.addToDB(DBUtility.get().getDb_());
        sleep(400);
    }

    @Override
    public void initializeView(){
        TodoListDbHelper helper = new TodoListDbHelper(testRule_.getActivity().getApplicationContext());
        helper.getWritableDatabase().delete(TodoListContract.TodolistEntry.TABLE_NAME, null, null);

        TodoListDBUtility.addPost(helper, user_.getGoogleId_(), postInBoth_.getKey_());
        TodoListDBUtility.addPost(helper, user_.getGoogleId_(), postOnlyInLocalDB_.getKey_());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_todoList));
    }


    @Test
    public void postInLocalDBAndFirebaseDBIsDisplayed(){
        onView(withId(R.id.recyclerview_todofragment)).check(matches(hasChildCount(1)));
        onView(withId(R.id.recyclerview_todofragment)).check(matches((hasDescendant(withText(postInBoth_.getTitle_())))));
        onView(withId(R.id.recyclerview_todofragment)).check(matches((hasDescendant(withText(postInBoth_.getBody_())))));
    }

    @Test
    public void postNotInFirebaseDBIsNotDisplayed(){
        onView(withId(R.id.recyclerview_todofragment)).check(matches(not(hasDescendant(withText(postOnlyInLocalDB_.getTitle_())))));
        onView(withId(R.id.recyclerview_todofragment)).check(matches(not(hasDescendant(withText(postOnlyInLocalDB_.getBody_())))));
    }

    @Test
    public void canCheckTodoDeletion() {
        onView(withId(R.id.recyclerview_todofragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.checkbox_todolist_layout)));
    }

    @Test
    public void clickOnTodoPostArriveAtPostActivity() {
        onView(withId(R.id.recyclerview_todofragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.textview_todolistadapter_body)));
        sleep(500);
        onView(withId(R.id.framelayout_postactivity_header));
    }
}
