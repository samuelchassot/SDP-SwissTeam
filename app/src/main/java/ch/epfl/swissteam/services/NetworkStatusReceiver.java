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

public class NetworkStatusReceiver extends BroadcastReceiver {

    private Activity activity_;

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkUtility.getConnectivityStatus(context);
        if("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())){
        Log.e("NetworkChange", "onReceive triggered");
            if (status == NetworkUtility.NOT_CONNECTED){
                Toast.makeText(context, "No Internet connection!", Toast.LENGTH_SHORT).show();
                setStatusBarColor(R.color.no_network);
            } else {
                setStatusBarColor(R.color.colorPrimaryDark);

            }
        }
    }

    public void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity_.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity_.getResources().getColor(color));
        }
    }

    public void setActivity_(Activity activity){
        this.activity_ = activity;
    }

}
