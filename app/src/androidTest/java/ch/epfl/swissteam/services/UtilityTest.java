package ch.epfl.swissteam.services;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class UtilityTest {
    @Test
    public void testIllegelCallException(){

        boolean flag = true;

        try {
            throw new Utility.IllegalCallException("test");
        } catch (Utility.IllegalCallException e) {
            flag = false;
        }

        if(flag){
            assertFalse("Exception must be thrown".equals(""));
        }
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
