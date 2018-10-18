package ch.epfl.swissteam.services;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NewProfileCapabilitiesTest {
    @Rule
    public final IntentsTestRule<NewProfileCapabilities> mActivityRule =
            new IntentsTestRule<>(NewProfileCapabilities.class);

    @Before
    public void setDbOffline() {
        TestUtils.setMock();
    }

    @Test
    public void canGoToNext() {
        onView(withId(R.id.button_newprofilecapabilites_done)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void canCheckCapability() {
        onView(withId(R.id.recyclerview_newprofilecapabilities_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId(R.id.checkbox_capabilitylayout_check)));
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