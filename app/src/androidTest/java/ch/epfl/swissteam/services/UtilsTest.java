package ch.epfl.swissteam.services;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Test;

import ch.epfl.swissteam.services.utils.Utils;

import static org.junit.Assert.assertFalse;

public class UtilsTest {
    @Test
    public void testIllegelCallException(){

        boolean flag = true;

        try {
            throw new Utils.IllegalCallException("test");
        } catch (Utils.IllegalCallException e) {
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
