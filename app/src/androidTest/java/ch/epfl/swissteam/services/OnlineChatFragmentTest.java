package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class OnlineChatFragmentTest extends FirebaseTest {

    private static final String mGoogleId = "1234";
    private static final String oGoogleId = "5678";
    private static final String url = TestUtils.getATestUser().getImageUrl_();
    private static final User mUser = new User(mGoogleId,"Bear", "polar@north.nth","",null,url);
    private static final User oUser = new User(oGoogleId, "Raeb", "hairy@north.nth", "", null, url);
    private static final ChatRelation chatRelation = new ChatRelation(mUser,oUser);

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize(){
        GoogleSignInSingleton.putUniqueID(mGoogleId);
        chatRelation.addToDB(DBUtility.get().getDb_());
        mUser.addChatRelation(chatRelation, DBUtility.get().getDb_());
        oUser.addChatRelation(chatRelation, DBUtility.get().getDb_());

        Intents.init();
    }

    @Test
    public void relationIsDisplayed() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_chats));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(oUser.getName_()))));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(oUser.getName_())).perform(click());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent(ChatRoom.class.getName()));
    }

    /* Examples
        - onView(nthChildOf(withId(R.id.recycleview), 0).check(matches(hasDescendant(withText("Some text"))))
        - onView(withId(R.id.recycleview)).check(matches(hasDescendant(withText("Some text"))))
     */
}
