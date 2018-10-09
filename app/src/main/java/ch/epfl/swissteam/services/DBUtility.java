package ch.epfl.swissteam.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DBUtility {

    private DatabaseReference db_;
    private static DBUtility instance;
    private final String USERS = "Users";
    private final String CATEGORIES = "Categories";
    private final String POSTS = "Posts";
    private final String ERROR_TAG = "DBUtility";
    private final int POSTS_DISPLAY_NUMBER = 20;

    private DBUtility(DatabaseReference db_){
        this.db_ = db_;
    }

    /**
     * Get the DBUtility instance
     * @return the DBUtiliyty instance
     */
    public static DBUtility get(){
        if (instance == null){
            instance = new DBUtility(FirebaseDatabase.getInstance().getReference());
        }
        return instance;
    }

    /**
     * Get the DatabaseReference of the DBUtility
     * @return the DatabaseReference of the DBUtility
     */
    public DatabaseReference getDb_(){
        return db_;
    }


    /**
     * Get all users' ID for a given category
     * @param category the category
     * @param callBack the CallBack to use
     */
    public void getUsersFromCategory(String category, final MyCallBack<ArrayList<String>> callBack){
        db_.child(CATEGORIES).child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> users = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    users.add(data.getKey());
                }
                callBack.onCallBack(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    /**
     * Get a user whose ID correspond to googleId
     * @param googleId unique user'Id
     * @param callBack the CallBack to use
     */
    public void getUser(String googleId, final MyCallBack<User> callBack) {
        db_.child(USERS).child(googleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callBack.onCallBack(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Get all users inside the database
     * @param callBack the callBack to use
     */
    public void getAllUsers(final MyCallBack<ArrayList<User>> callBack){

        db_.child(USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<User> users = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Log.i("USERSDB", data.getValue(User.class).getName_());
                    users.add(data.getValue(User.class));
                }
                callBack.onCallBack(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    /**
     * Retrieves the ten freshest post of the database
     * @param callBack the function called on the callBack
     */
    public void getPostsFeed(final MyCallBack<ArrayList<Post>> callBack){
        Query freshestPosts = db_.child(POSTS).limitToFirst(POSTS_DISPLAY_NUMBER);
        freshestPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Post> posts = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Log.e("POSTSDB", "test "+data.getValue(Post.class));
                    Post post = data.getValue(Post.class);
                    posts.add(post);
                }
                callBack.onCallBack(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ERROR_TAG, "getPostsFeed:onCancelled", databaseError.toException());
            }
        });


    public void setUser(User user){
        db_.child(USERS).child(user.getGoogleId_()).setValue(user);

    }
}
