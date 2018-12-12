package ch.epfl.swissteam.services.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.epfl.swissteam.services.providers.DBCallBack;
import ch.epfl.swissteam.services.R;

public class Utils {

    /**
     * Transform a string into a date. String must have the format "yyyy-MM-dd"
     * @param dateString
     * @return a Date object (null if string's format is not the required)
     */
    public static Date dateFromString(String dateString){
        if(dateString == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Transform a Date object into a String with format "yyyy-MM-dd"
     * @param date the Date object to transform
     * @return the String in the required format (null if date == null)
     */
    public static String dateToString(Date date){
        if(date == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(date);
        return format;
    }

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
