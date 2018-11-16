package ch.epfl.swissteam.services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Broadcast receiver for network status.
 */
public class NetworkStatusReceiver extends BroadcastReceiver {

    private Activity activity_;

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkUtility.getConnectivityStatus(context);
        int darkmode = SettingsDBUtility.retrieveDarkMode(new SettingsDbHelper(activity_.getApplicationContext()),GoogleSignInSingleton.get().getClientUniqueID());
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            Log.i("NetworkChange", "onReceive triggered");
            if (status == NetworkUtility.NOT_CONNECTED) {
                Toast.makeText(context, "No Internet connection!", Toast.LENGTH_SHORT).show();
                setStatusBarColor(R.color.no_network);
            } else {
                if (darkmode == 1){
                    setStatusBarColor(R.color.DarkStatusBar);
                } else {
                    setStatusBarColor(R.color.colorPrimaryDark);
                }

            }
        }
    }

    /**
     * Set the status bar's color given a color in parameter
     *
     * @param color color to use
     */
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity_.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity_.getResources().getColor(color));
        }
    }

    /**
     * Set the activity that is check in the NetworkStatusReceiver
     *
     * @param activity an activity
     */
    public void setActivity_(Activity activity) {
        this.activity_ = activity;
    }

}
