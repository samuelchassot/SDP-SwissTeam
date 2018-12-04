package ch.epfl.swissteam.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.util.SortedList;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Interface that provides some static methods to interact with the local DB for the posts in the TODOList.
 *
 * @author Julie Giunta
 */
public class TodolistDBUtility {

    /**
     * Add a row in the local TodolistDB which has userID and postID as values.
     *
     * @param helper a TodolistDBHelper created with the context of the caller,
     * @param userID the id of the currently logged in user,
     * @param postID the key of the post to add to the local DB.
     *
     * @return the key of the new row added to the local DB.
     */
    static long addPost(TodolistDbHelper helper, String userID, String postID){
        SQLiteDatabase db = helper.getWritableDatabase();

        //Value which will be stored as a row in the local DB
        ContentValues values = new ContentValues();
        values.put(TodolistContract.TodolistEntry.COLUMN_ID, userID);
        values.put(TodolistContract.TodolistEntry.COLUMN_POSTS, postID);

        //Store the new row in the DB
        long rowKey = db.insert(TodolistContract.TodolistEntry.TABLE_NAME,
                null,
                values);
        db.close();
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
    static int deletePost(TodolistDbHelper helper, String userID, String postID){
        SQLiteDatabase db = helper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = TodolistContract.TodolistEntry.COLUMN_ID + " LIKE ? AND "
                + TodolistContract.TodolistEntry.COLUMN_POSTS + "LIKE ? ";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { userID, postID };

        int deletedRows = db.delete(TodolistContract.TodolistEntry.TABLE_NAME, selection, selectionArgs);
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
    static void getPosts(TodolistDbHelper helper, String userID, DBCallBack<Post> callback){
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {TodolistContract.TodolistEntry.COLUMN_POSTS};

        String selection = TodolistContract.TodolistEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { userID };

        try{
            Cursor cursor = db.query(
                    TodolistContract.TodolistEntry.TABLE_NAME,
                    projection, selection, selectionArgs,
                    null, null, null
            );

            while(cursor.moveToNext()){
                String postID = cursor.getString(
                        cursor.getColumnIndexOrThrow(TodolistContract.TodolistEntry.COLUMN_POSTS));
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

}
