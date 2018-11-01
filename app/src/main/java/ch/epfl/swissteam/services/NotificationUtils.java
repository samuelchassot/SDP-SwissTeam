package ch.epfl.swissteam.services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Utility class for methods related to notifications.
 *
 * @author Adrian Baudat
 */
public class NotificationUtils {

    public static String CUSTOM_NOTIFICATION_CHANNEL_ID = "CUSTOM_NOTIFICATION";

    public static void sendCustomNotification(Activity activity, String textTitle, String textContent){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, CUSTOM_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_google)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(activity).notify(1, mBuilder.build());
    }
}
