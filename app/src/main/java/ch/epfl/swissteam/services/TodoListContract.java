package ch.epfl.swissteam.services;

import android.provider.BaseColumns;

/**
 * Contract defining the database table name and column names
 * for a single table representing the posts in the todolist of the user
 *
 * @author Julie Giunta
 */
public class TodoListContract {
    public static final String LOCAL_DB_NAME = "ch.epfl.swissteam.services.todolistlocalDB";
    public static final int DB_VERSION = 1;

    private TodoListContract(){}

    public static class TodolistEntry implements BaseColumns{
        public static final String TABLE_NAME = "entry";

        //Column for the id of the logged in user
        public static final String COLUMN_ID = "id";

        //Column for the key of the post
        public static final String COLUMN_POSTS = "post";

    }
}
