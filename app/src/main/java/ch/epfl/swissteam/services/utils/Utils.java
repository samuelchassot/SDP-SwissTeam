package ch.epfl.swissteam.services.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;

import ch.epfl.swissteam.services.providers.DBCallBack;
import ch.epfl.swissteam.services.R;

public class Utils {
    /**
     * exception to prevent invalid calls
     */
    public static class IllegalCallException extends Exception{
        public IllegalCallException(String m){
            super(m);
        }
    }

    /**
     * alert dialog with a title 'title' that ask 'message' and as two buttons, on 'delete' and one 'cancel'
     * if delete is pressed then deletionCallBack callback true, false otherwise
     * @param context the context for this dialog to appear
     * @param title the title of the alert
     * @param message the message of the alert
     * @param deletionCallback call back with true if deleted has been selected, false if canceled,
     *                         this callBack needs to delete
     */
    public static void askToDeleteAlertDialog(Context context, String title,
                                              String message, DBCallBack<Boolean> deletionCallback){
        Resources res = context.getResources();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, res.getString(R.string.general_delete),
                (dialog, which) -> {
                    dialog.dismiss();
                    deletionCallback.onCallBack(true);
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, res.getString(R.string.general_cancel),
                (dialog, which) -> {
                    dialog.dismiss();
                    deletionCallback.onCallBack(false);
                });

        alertDialog.show();
    }
}
