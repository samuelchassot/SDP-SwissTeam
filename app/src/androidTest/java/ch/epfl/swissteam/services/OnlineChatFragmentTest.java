package ch.epfl.swissteam.services;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.init;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.swissteam.services.TestUtils.sleep;

/**
 * Tests for OnlineChatFragment.
 *
 * @author Sébastien Gachoud
 */
public class OnlineChatFragmentTest extends FirebaseTest {

    private static final String mGoogleId = "1234";
    private static final String oGoogleId = "5678";
    private static final String url = TestUtils.getTestUser().getImageUrl_();
    private static final User mUser = new User(mGoogleId,"Bear", "polar@north.nth","",
            null, null, url,0,0,0,null,null);
    private static final User oUser = new User(oGoogleId, "Raeb", "hairy@north.nth", "",
            null, null, url,0,0,0,null,null);
    private static final User tUser = new User("9101", "Petra", "petra@gmail.com","",
            null, null, url, 0, 0, 0, null, null);
    private static final ChatRelation chatRelation = new ChatRelation(mUser,oUser);
    private static final ChatRelation chatRelation2 = new ChatRelation(mUser,tUser);
    private static final ChatRelation chatRelation3 = new ChatRelation(oUser,tUser);

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize(){
        LocationManager.get().setMock();
        GoogleSignInSingleton.putUniqueID(mGoogleId);
        chatRelation.addToDB(DBUtility.get().getDb_());
        chatRelation2.addToDB(DBUtility.get().getDb_());
        chatRelation3.addToDB(DBUtility.get().getDb_());
        mUser.addChatRelation(chatRelation, DBUtility.get().getDb_());
        mUser.addChatRelation(chatRelation2, DBUtility.get().getDb_());
        oUser.addChatRelation(chatRelation, DBUtility.get().getDb_());
        oUser.addChatRelation(chatRelation3, DBUtility.get().getDb_());
        tUser.addChatRelation(chatRelation2, DBUtility.get().getDb_());
        tUser.addChatRelation(chatRelation3, DBUtility.get().getDb_());

        Intents.init();


        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        sleep(100);
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_chats));
        sleep(100);
    }

    @Override
    public void terminate() {
        Intents.release();
        LocationManager.get().unsetMock();
    }

    @Test
    public void relationIsDisplayed() {
        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(oUser.getName_()))));
        sleep(100);
        onView(withText(oUser.getName_())).check(matches(isDisplayed()));


        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(tUser.getName_()))));
        sleep(100);
        onView(withText(tUser.getName_())).check(matches(isDisplayed()));
    }

    @Test
    public void relationCanOpenChat() {
        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(oUser.getName_()))));
        sleep(100);
        onView(withText(oUser.getName_())).perform(click());
        sleep(100);
        intended(hasComponent(ChatRoom.class.getName()));
    }

    @Test
    public void canChooseToDeleteRelation() {
        onView(withText(oUser.getName_())).perform(longClick());
        sleep(100);
        onView(withText(mainActivityRule_.getActivity().getResources().getString(R.string.chat_relation_delete_alert_text)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canDeleteRelation() {
        onView(withText(oUser.getName_())).perform(longClick());
        sleep(100);
        onView(withText(mainActivityRule_.getActivity().getResources().getString(R.string.general_delete))).perform(click());
        onView(withText(oUser.getName_())).check(doesNotExist());
    }

    @Test
    public void canCancelDeleteRelation() {
        onView(withText(oUser.getName_())).perform(longClick());
        sleep(100);
        onView(withText(mainActivityRule_.getActivity().getResources().getString(R.string.chat_relation_delete_alert_text)))
                .check(matches(isDisplayed()));
        onView(withText(mainActivityRule_.getActivity().getResources().getString(R.string.general_cancel))).perform(click());
        onView(withText(oUser.getName_())).check(matches(isDisplayed()));
    }

    /*-----Search bar tests-----*/

    @Test
    public void searchNothingDisplaysAll(){
        Espresso.closeSoftKeyboard();
        String text = "";
        onView(withId(R.id.online_chat_fragment_search_bar)).perform(typeText(text), closeSoftKeyboard()).check(matches(withText(text)));

        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(oUser.getName_()))));
        sleep(100);
        onView(withText(oUser.getName_())).check(matches(isDisplayed()));

        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(tUser.getName_()))));
        sleep(100);
        onView(withText(tUser.getName_())).check(matches(isDisplayed()));
    }

    @Test
    public void searchForInexistentDisplaysNothing(){
        Espresso.closeSoftKeyboard();
        String text = "Lucifer";
        onView(withId(R.id.online_chat_fragment_search_bar)).perform(typeText(text), closeSoftKeyboard()).check(matches(withText(text)));

        onView(withText(oUser.getName_())).check(doesNotExist());

        onView(withText(tUser.getName_())).check(doesNotExist());
    }

    @Test
    public void searchForExistentDisplaysSearch(){
        Espresso.closeSoftKeyboard();
        String text = "Ra";
        onView(withId(R.id.online_chat_fragment_search_bar)).perform(typeText(text), closeSoftKeyboard()).check(matches(withText(text)));

        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(oUser.getName_()))));
        sleep(100);
        onView(withText(oUser.getName_())).check(matches(isDisplayed()));

        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(tUser.getName_()))));
        sleep(100);
        onView(withText(tUser.getName_())).check(matches(isDisplayed()));
    }

    @Test
    public void searchForExistentDisplaysSearchButNotOther(){
        Espresso.closeSoftKeyboard();
        String text = "pet";
        onView(withId(R.id.online_chat_fragment_search_bar)).perform(typeText(text), closeSoftKeyboard()).check(matches(withText(text)));

        onView(withText(oUser.getName_())).check(doesNotExist());

        onView(withId(R.id.fragment_online_chats_recycler_view)).check(matches(hasDescendant(withText(tUser.getName_()))));
        sleep(100);
        onView(withText(tUser.getName_())).check(matches(isDisplayed()));
    }


    /* Examples
        - onView(nthChildOf(withId(R.id.recycleview), 0).check(matches(hasDescendant(withText("Some text"))))
        - onView(withId(R.id.recycleview)).check(matches(hasDescendant(withText("Some text"))))
     */
}
