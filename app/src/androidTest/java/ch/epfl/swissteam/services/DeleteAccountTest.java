package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import ch.epfl.swissteam.services.models.ChatMessage;
import ch.epfl.swissteam.services.models.ChatRelation;
import ch.epfl.swissteam.services.models.Post;
import ch.epfl.swissteam.services.models.User;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.providers.GoogleSignInSingleton;
import ch.epfl.swissteam.services.utils.Utils;
import ch.epfl.swissteam.services.view.activities.MainActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.swissteam.services.TestUtils.sleep;
import static org.junit.Assert.assertEquals;

public class DeleteAccountTest extends SocializeTest<MainActivity> {

    public DeleteAccountTest() {
        setTestRule(MainActivity.class);
    }

    @Override
    public void initialize() {
        User testUser = TestUtils.getTestUser();
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(testUser.getGoogleId_());
    }

    @Override
    public void initializeView(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInSingleton.putGoogleSignInClient(GoogleSignIn.getClient(testRule_.getActivity().getApplicationContext(), gso));
    }

    @Test
    public void deleteCurrentUser(){
        User testUser = TestUtils.getTestUser();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));
        onView(withId(R.id.button_settings_deleteaccount)).perform(scrollTo()).perform(click());
        sleep(1000);

        onView(withId(R.id.edittext_deleteaccount_continue)).perform(typeText("CONTINUE"));
        closeSoftKeyboard();
        sleep(1000);
        onView(withId(R.id.button_deleteaccount_deletebutton)).perform(click());
        sleep(5000);

        DBUtility.get().getUser(testUser.getGoogleId_(), u->{
            assertEquals(u, null);
        });
    }

    @Test
    public void addPostThenDeleteUser(){
        initialize();
        User testUser = TestUtils.getTestUser();
        Date today = Calendar.getInstance().getTime();
        Post post = new Post("1234_1539704399119", "Title", testUser.getGoogleId_(), "Body",
                1539704399119L,  10, 20, Utils.dateToString(today));
        DBUtility.get().setPost(post);
        sleep(1000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));
        onView(withId(R.id.button_settings_deleteaccount)).perform(scrollTo()).perform(click());
        sleep(1000);

        onView(withId(R.id.edittext_deleteaccount_continue)).perform(typeText("CONTINUE"));
        closeSoftKeyboard();
        sleep(1000);
        onView(withId(R.id.button_deleteaccount_deletebutton)).perform(click());
        sleep(5000);

        DBUtility.get().getUser(testUser.getGoogleId_(), u->{
            assertEquals(u, null);
        });
    }

    @Test
    public void addMessagesThenDeleteUser(){
        User firstUser = TestUtils.getTestUser();
        User secondUser = TestUtils.O_USER;
        secondUser.addToDB(DBUtility.get().getDb_());
        sleep(1000);
        ChatRelation newRelation = new ChatRelation(firstUser, secondUser);
        newRelation.addToDB(DBUtility.get().getDb_());
        firstUser.addChatRelation(newRelation,  DBUtility.get().getDb_());
        secondUser.addChatRelation(newRelation, DBUtility.get().getDb_());
        sleep(1000);

        String testMessage = "Goodbye !";
        ChatMessage chatMessage = new ChatMessage(testMessage, firstUser.getName_(), firstUser.getGoogleId_(), newRelation.getId_());
        chatMessage.addToDB(DBUtility.get().getDb_());
        sleep(1000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.button_maindrawer_settings));
        onView(withId(R.id.button_settings_deleteaccount)).perform(scrollTo()).perform(click());
        sleep(1000);

        onView(withId(R.id.edittext_deleteaccount_continue)).perform(typeText("CONTINUE"));
        closeSoftKeyboard();
        sleep(1000);
        onView(withId(R.id.button_deleteaccount_deletebutton)).perform(click());
        sleep(5000);

        DBUtility.get().getUser(firstUser.getGoogleId_(), u->{
            assertEquals(u, null);
        });

    }
}
