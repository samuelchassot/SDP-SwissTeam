package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.swissteam.services.TestUtils.sleep;
import static org.junit.Assert.assertEquals;

public class DeleteAccountTest extends FirebaseTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule_ =
            new ActivityTestRule<>(MainActivity.class);

    @Override
    public void initialize() {
        super.initialize();
        LocationManager.get().setMock();
        User testUser = TestUtils.getTestUser();
        testUser.addToDB(DBUtility.get().getDb_());
        GoogleSignInSingleton.putUniqueID(testUser.getGoogleId_());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInSingleton.putGoogleSignInClient(GoogleSignIn.getClient(mainActivityRule_.getActivity().getApplicationContext(), gso));

    }

    @Override
    public void terminate() {
        super.terminate();
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
        Post post = new Post("1234_1539704399119", "Title", testUser.getGoogleId_(), "Body", 1539704399119L,  10, 20);
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
}
