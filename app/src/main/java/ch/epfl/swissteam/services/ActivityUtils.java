package ch.epfl.swissteam.services;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * This class contains static method that are useful when working with activities
 */
public class ActivityManager {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null) {
            v = new View(activity);
        }
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
