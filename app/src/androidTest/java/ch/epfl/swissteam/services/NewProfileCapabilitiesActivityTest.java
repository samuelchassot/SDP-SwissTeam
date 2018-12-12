package ch.epfl.swissteam.services;

import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.Test;

import ch.epfl.swissteam.services.view.activities.NewProfileCapabilitiesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.swissteam.services.UtilsTest.clickChildViewWithId;

public class NewProfileCapabilitiesActivityTest extends SocializeTest<NewProfileCapabilitiesActivity>{

    public NewProfileCapabilitiesActivityTest(){
        setTestRule(NewProfileCapabilitiesActivity.class);
    }

    @Test
    public void canCheckCapability() {
        onView(withId(R.id.recyclerview_newprofilecapabilities_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId(R.id.checkbox_capabilitylayout_check)));
    }
}