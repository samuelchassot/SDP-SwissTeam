package ch.epfl.swissteam.services;


import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;

import static ch.epfl.swissteam.services.DBUtility.*;

public class Utility {
    /**
     * exception to prevent invalid calls
     */
    public static class IllegalCallException extends Exception{
        IllegalCallException(String m){
            super(m);
        }
    }

    /**
     * alert dialog with a title 'title' that ask 'message' and as two buttons, on 'delete' and one 'cancel'
     * if delete is pressed then 'savable' is removed from the database, if cancel is pressed then nothing happens
     * @param context the context for this dialog to appear
     * @param savable the object to delete
     * @param child the child of the object to delete or null if it is the savable itself that must be deleted
     * @param title the title of the alert
     * @param message the message of the alert
     * @param callback call back with true if deleted, false if canceled
     */
    public static void askToDeleteAlertDialog(Context context, DBSavable savable, String child, String title,
                                              String message, DBCallBack<Boolean> callback){
        Resources res = context.getResources();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, res.getString(R.string.general_delete),
                (dialog, which) -> {
                    dialog.dismiss();
                    try {
                        savable.removeFromDB(get().getDb_(), child);
                    } catch (IllegalCallException e) {
                        e.printStackTrace();
                    }
                    callback.onCallBack(true);
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, res.getString(R.string.general_cancel),
                (dialog, which) -> {
                    dialog.dismiss();
                    callback.onCallBack(false);
                });

        alertDialog.show();
    }

    public static void askToDeleteAlertDialog(Context context, DBSavable savable, String child,
                                              String title, String message){
        askToDeleteAlertDialog(context, savable, child, title, message,(b) ->{});
    }
}
