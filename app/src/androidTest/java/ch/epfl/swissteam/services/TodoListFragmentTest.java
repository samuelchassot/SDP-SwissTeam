package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.swissteam.services.TestUtils.sleep;

public class TodoListFragmentTest extends SocializeTest<MainActivity> {

    public TodoListFragmentTest(){
        setTestRule(MainActivity.class);
    }

    @Override
    public void initialize() {
        /*TestUtils.addTestPost();
        User user = TestUtils.getTestUser();
        Post post = TestUtils.getTestPost();
        GoogleSignInSingleton.putUniqueID(user.getGoogleId_());
        post.addToDB(DBUtility.get().getDb_());
        user.addToDB(DBUtility.get().getDb_());
        sleep(400);*/
    }

    @Test
    public void canOpenTodoListFragment(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_todoList));
    }
}
