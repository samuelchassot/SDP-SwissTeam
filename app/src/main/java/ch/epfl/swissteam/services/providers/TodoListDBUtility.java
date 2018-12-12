package ch.epfl.swissteam.services.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ch.epfl.swissteam.services.models.Post;
import ch.epfl.swissteam.services.providers.DBCallBack;
import ch.epfl.swissteam.services.providers.DBUtility;
import ch.epfl.swissteam.services.utils.TodoListContract;
import ch.epfl.swissteam.services.utils.TodoListDbHelper;


/**
 * Interface that provides some static methods to interact with the local DB for the posts in the TodoList.
 *
 * @author Julie Giunta
 */
public class TodoListDBUtility {

    /**
     * Add a row in the local TodolistDB which has userID and postID as values.
     * It adds the row only if there is not a row with these values yet
     *
     * @param helper a TodolistDBHelper created with the context of the caller,
     * @param userID the id of the currently logged in user,
     * @param postID the key of the post to add to the local DB.
     *
     * @return the key of the new row added to the local DB,
     * 0 if it was already in the DB, -1 if there was a problem adding the row.
     */
    public static long addPost(TodoListDbHelper helper, String userID, String postID){
        long rowKey = 0;
        if(!isPostInDB(helper, userID, postID)){
            SQLiteDatabase db = helper.getWritableDatabase();

            //Value which will be stored as a row in the local DB
            ContentValues values = new ContentValues();
            values.put(TodoListContract.TodolistEntry.COLUMN_ID, userID);
            values.put(TodoListContract.TodolistEntry.COLUMN_POSTS, postID);

            //Store the new row in the DB
            rowKey = db.insert(TodoListContract.TodolistEntry.TABLE_NAME,
                    null,
                    values);
            db.close();
        }
        return rowKey;
    }

    /**
     * Deletes rows in the local TodolistDB which have userID and postID as values.
     *
     * @param helper a TodolistDBHelper created with the context of the caller,
     * @param userID the id of the currently logged in user,
     * @param postID the key of the post to add to the local DB.
     *
     * @return the number of rows deleted.
     */
    public static int deletePost(TodoListDbHelper helper, String userID, String postID){
        SQLiteDatabase db = helper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = TodoListContract.TodolistEntry.COLUMN_ID + " LIKE ? AND "
                + TodoListContract.TodolistEntry.COLUMN_POSTS + " LIKE ? ";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { userID, postID };

        int deletedRows = db.delete(TodoListContract.TodolistEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return deletedRows;
    }

    /**
     * Retrieve the posts that the user has stocked in his todolist.
     *
     * @param helper a TodolistDBHelper created with the context of the caller,
     * @param userID the id of the currently logged in user,
     * @param callback a callback function to use whenever we retrieve a post.
     */
    public static void getPosts(TodoListDbHelper helper, String userID, DBCallBack<Post> callback){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {TodoListContract.TodolistEntry.COLUMN_POSTS};

        String selection = TodoListContract.TodolistEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { userID };

        try{
            Cursor cursor = db.query(
                    TodoListContract.TodolistEntry.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, null
            );

            while(cursor.moveToNext()){
                String postID = cursor.getString(
                        cursor.getColumnIndexOrThrow(TodoListContract.TodolistEntry.COLUMN_POSTS));
                DBUtility.get().getPost(postID, p -> callback.onCallBack(p));
            }

            cursor.close();
        }catch(IllegalArgumentException e){
            Log.e("TODOLISTDBUTILITY", "Column does not exist when retrieving posts");
        }catch(Exception e){
            Log.e("TODOLISTDBUTILITY", "Could not query the DB");
        }

        db.close();
    }

    public static boolean isPostInDB(TodoListDbHelper helper, String userID, String postID){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                TodoListContract.TodolistEntry.COLUMN_POSTS
        };

        // Define 'where' part of query.
        String selection = TodoListContract.TodolistEntry.COLUMN_ID + " = ? AND "
                + TodoListContract.TodolistEntry.COLUMN_POSTS + " = ? ";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { userID, postID };


        Cursor cursor = db.query(
                TodoListContract.TodolistEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause (selection)
                selectionArgs,          // The values for the WHERE clause (selectionArgs)
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order (sortOrder)
        );

        boolean exists = false;

        if(cursor.moveToFirst()){
            exists = true;
        }
        cursor.close();
        db.close();

        return exists;
    }

}
