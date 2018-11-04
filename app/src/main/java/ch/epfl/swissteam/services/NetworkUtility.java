package ch.epfl.swissteam.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Class containing utilities related to the network.
 */
public class NetworkUtility {

    public static final int CONNECTED = 0;
    public static final int NOT_CONNECTED = 1;

    /**
     * Returns the current internet connectivity status.
     *
     * @param context calling context
     * @return current internet connectivity
     */
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return CONNECTED;
            }
        }
        return NOT_CONNECTED;
    }
}
