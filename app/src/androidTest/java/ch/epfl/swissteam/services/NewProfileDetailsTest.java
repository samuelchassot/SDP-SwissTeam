package ch.epfl.swissteam.services;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class NewProfileDetailsTest extends FirebaseTest{

    private static final String username = "Jean-Claude",
            firstName = "Jean",
            lastName = "Claude",
            id = "1234",
            description = "J'ai 65 ans et j'aime les pommes.",
    photoUrl = "http://static.javadoc.io/org.mockito/mockito-core/2.22.0/org/mockito/logo@2x.png",
    longDescription = "\n" +
            "\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut a convallis urna, quis efficitur tellus. Pellentesque et aliquet lorem, efficitur efficitur mauris. Aenean at finibus neque. Integer tincidunt accumsan elit a lobortis. Vivamus eu purus aliquet, tempor mi in, fringilla arcu. Pellentesque imperdiet pulvinar neque at posuere. Maecenas sed mi eu leo vestibulum elementum aliquet sed ipsum. Nam congue in mauris a porttitor. Integer pellentesque mauris justo, dictum tempor elit ultrices eget.\n" +
            "\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam sed vestibulum felis. Suspendisse cursus ultrices diam nec porttitor. Vestibulum congue, tellus et mollis cursus, eros massa convallis nisi, id pharetra turpis dui eu orci. In non eleifend enim. Vestibulum ornare neque diam, bibendum hendrerit est rutrum quis. Fusce aliquam fringilla ornare. Nunc ultrices, nisi id ultricies euismod, dui nunc pellentesque felis, sit amet faucibus ligula erat at enim. Donec ultrices leo nec metus placerat, eu venenatis metus sagittis. Nullam massa erat, auctor ac dui tristique, aliquam vulputate nulla. Quisque dignissim efficitur magna quis porta. Morbi condimentum erat tellus, eget commodo massa faucibus quis.\n" +
            "\n" +
            "Aliquam bibendum tellus ac urna suscipit viverra. Curabitur feugiat nisl nisi, non tincidunt tortor pulvinar ut. Phasellus cursus ligula eget dui ornare, porttitor imperdiet mauris aliquet. Suspendisse et neque tellus. Sed sit amet justo volutpat, consequat mauris et, viverra massa. Quisque mattis laoreet efficitur. Cras consectetur interdum leo eget maximus. Ut ornare magna a ligula faucibus iaculis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum id felis eu quam bibendum aliquam non et est. Morbi nisi dui, vehicula in ex a, elementum interdum elit.\n" +
            "\n" +
            "Proin massa ante, bibendum sagittis nisi at, consectetur pretium metus. Aenean at malesuada leo. Integer at massa id nulla aliquet fermentum at eget mauris. Cras imperdiet mi ac nisi eleifend consectetur. Praesent non dignissim justo, accumsan sollicitudin erat. Quisque maximus euismod lorem sed tincidunt. Aliquam dignissim posuere ligula, vel laoreet urna auctor vitae.\n" +
            "\n" +
            "Phasellus sit amet commodo orci. Ut id nulla quis metus sodales dapibus. Suspendisse et dui ac risus rhoncus mollis ut vehicula risus. Donec at sem bibendum, molestie mi quis, gravida orci. Quisque eu vehicula purus. Suspendisse aliquam turpis et magna malesuada posuere. Interdum et malesuada fames ac ante ipsum primis in faucibus. Duis accumsan vulputate sem, et tincidunt arcu mollis at. Donec quis libero posuere, mattis justo ac, lacinia nulla. Duis vel diam vel sapien ultrices sollicitudin eu id velit. Quisque interdum leo sed massa venenatis sagittis. Fusce eget consequat tortor. Vivamus in convallis elit, sit amet placerat ex. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. ";

    @Rule
    public final IntentsTestRule<NewProfileDetails> mActivityRule =
            new IntentsTestRule<>(NewProfileDetails.class);

    @Override
    public void initialize() {
        LocationManager.get().setMock();
    }

    @Override
    public void terminate() {
        LocationManager.get().unsetMock();
    }

    @Test
    public void testTypingName() {
        onView(withId(R.id.plaintext_newprofiledetails_name)).perform(typeText(username)).check(matches(withText(username)));
    }

    @Test
    public void testTypingDescription() {
        onView(withId(R.id.plaintext_newprofiledetails_description)).perform(typeText(description)).check(matches(withText(description)));
    }

    @Test
    public void typeDescriptionThenNext() {
        onView(withId(R.id.plaintext_newprofiledetails_description)).perform(replaceText(longDescription));
        onView(withId(R.id.plaintext_newprofiledetails_description)).perform(typeText(description), closeSoftKeyboard());
        onView(withId(R.id.button_newprofiledetails_next)).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(NewProfileCapabilities.class.getName()));
    }

    @Test
    public void connectWithNullAccount() {
        GoogleSignInAccount acc = null;
        Intent i = new Intent();
        i.putExtra(SignInActivity.ACCOUNT_TAG, acc);
        mActivityRule.finishActivity();
        mActivityRule.launchActivity(i);
        onView(withId(R.id.plaintext_newprofiledetails_name)).check(matches(withText("")));
    }

    @Test
    public void createNewAccount() {
        GoogleSignInAccount acc = mock(GoogleSignInAccount.class);
        when(acc.getDisplayName()).thenReturn(username);
        when(acc.getPhotoUrl()).thenReturn(Uri.parse(photoUrl));
        when(acc.getId()).thenReturn(id);
        doCallRealMethod().when(acc).writeToParcel(any(Parcel.class), anyInt());
        Intent i = new Intent();
        i.putExtra(SignInActivity.ACCOUNT_TAG, acc);
        mActivityRule.finishActivity();
        mActivityRule.launchActivity(i);
        onView(withId(R.id.plaintext_newprofiledetails_name)).check(matches(withText(username)));
        onView(withId(R.id.button_newprofiledetails_next)).perform(click());
        intended(hasComponent(NewProfileCapabilities.class.getName()));
        onView(withId(R.id.button_newprofilecapabilites_done)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void connectWithMockedAccountNoDisplayName() {
        GoogleSignInAccount acc = mock(GoogleSignInAccount.class);
        when(acc.getDisplayName()).thenReturn(null);
        when(acc.getGivenName()).thenReturn(firstName);
        when(acc.getFamilyName()).thenReturn(lastName);
        when(acc.getPhotoUrl()).thenReturn(Uri.parse(photoUrl));
        doCallRealMethod().when(acc).writeToParcel(any(Parcel.class), anyInt());
        Intent i = new Intent();
        i.putExtra(SignInActivity.ACCOUNT_TAG, acc);
        mActivityRule.finishActivity();
        mActivityRule.launchActivity(i);
        onView(withId(R.id.plaintext_newprofiledetails_name)).check(matches(withText(firstName + " " + lastName)));
    }

    @Test
    public void connectWithMockedAccountNoName() {
        GoogleSignInAccount acc = mock(GoogleSignInAccount.class);
        when(acc.getDisplayName()).thenReturn(null);
        when(acc.getGivenName()).thenReturn(null);
        when(acc.getFamilyName()).thenReturn(null);
        when(acc.getPhotoUrl()).thenReturn(Uri.parse(photoUrl));
        doCallRealMethod().when(acc).writeToParcel(any(Parcel.class), anyInt());
        Intent i = new Intent();
        i.putExtra(SignInActivity.ACCOUNT_TAG, acc);
        mActivityRule.finishActivity();
        mActivityRule.launchActivity(i);
        onView(withId(R.id.plaintext_newprofiledetails_name)).check(matches(withText("")));
    }

    @Test
    public void connectWithMockedAccountWrongPhotoUrl() {
        GoogleSignInAccount acc = mock(GoogleSignInAccount.class);
        when(acc.getDisplayName()).thenReturn(null);
        when(acc.getGivenName()).thenReturn(null);
        when(acc.getFamilyName()).thenReturn(null);
        when(acc.getPhotoUrl()).thenReturn(Uri.parse(photoUrl + "hvehiuhe"));
        doCallRealMethod().when(acc).writeToParcel(any(Parcel.class), anyInt());
        Intent i = new Intent();
        i.putExtra(SignInActivity.ACCOUNT_TAG, acc);
        mActivityRule.finishActivity();
        mActivityRule.launchActivity(i);
        onView(withId(R.id.plaintext_newprofiledetails_name)).check(matches(withText("")));
    }
}