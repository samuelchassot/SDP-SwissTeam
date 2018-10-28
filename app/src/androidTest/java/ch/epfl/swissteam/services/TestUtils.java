package ch.epfl.swissteam.services;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class TestUtils {
    protected static final String M_GOOGLE_ID = "1234";
    protected static final String O_GOOGLE_ID = "456";
    protected static final String URL = TestUtils.getATestUser().getImageUrl_();
    protected static final ArrayList<Categories> CATS = new ArrayList<>(Arrays.asList(Categories.COOKING));
    protected static final User M_USER = new User(M_GOOGLE_ID,"Bear", "polar@north.nth","",null, URL,0);
    protected static final User O_USER = new User(O_GOOGLE_ID, "Raeb", "hairy@north.nth", "", CATS, URL, 0);

    protected static User getATestUser(){
        ArrayList<Categories> cat = new ArrayList<>();
        cat.add(Categories.IC);
        User testUser = new User("1234", "testuser", "test@gmail.com", "I am a test user", cat, "https://lh5.googleusercontent.com/-SYTkc6TIZHI/AAAAAAAAAAI/AAAAAAAAABc/EBrA4sSVWQc/photo.jpg");
        return testUser;
    }

    protected static void addTestPost() {
        long timestamp = (new Date()).getTime();
        String key = "1234" + "_" + timestamp;
        DBUtility.get().setPost(new Post(key, "Hello there", "1234", "General Kenobi", timestamp));


    }

    protected static Post getTestPost() {
        long timestamp = (new Date()).getTime();
        String key = "1234" + "_" + timestamp;
        return new Post(key, "Hello there", "1234", "General Kenobi", timestamp);
    }

    protected static void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with "+childPosition+" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }




    protected static void setMock(){
        DBUtility.get().getDb_().getDatabase().goOffline();
    }

    protected static void recyclerScrollToItemWithTextAndPerformOnItem(int recyclerViewId, String text, ViewAction perform){
        /*onView(withId(recyclerViewId)).
                perform(RecyclerViewActions.scrollTo(hasDescendant(withText(text))));*/
        sleep(2000);
        onView(withId(recyclerViewId)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(text)), perform));
    }

    protected static void recyclerScrollToItemWithTextAndPerformClickItem(int recyclerViewId, String text){
        recyclerScrollToItemWithTextAndPerformOnItem(recyclerViewId, text, click());
    }

    protected static ViewAction personalClick(){
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isEnabled(); // no constraints, they are checked above
            }

            @Override
            public String getDescription() {
                return "click plus button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick();
            }
        };
    }
}

