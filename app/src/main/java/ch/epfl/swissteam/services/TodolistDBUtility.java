package ch.epfl.swissteam.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface that provides some static methods to interact with the local DB for the posts in the TODOList.
 *
 * @author Julie Giunta
 */
public class TodolistDBUtility {

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

    static List<Post> getPosts(TodolistDbHelper helper, String userID){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Post> posts = new ArrayList<>();

        //TODO query
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
                //TODO Search in Firebase and add to posts
                //posts.add(postsID);
            }

            cursor.close();
        }catch(IllegalArgumentException e){
            Log.e("TODOLISTDBUTILITY", "Column does not exist when retrieving posts");
        }catch(Exception e){
            Log.e("TODOLISTDBUTILITY", "Could not query the DB");
        }

        db.close();
        return posts;
    }

}
