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

    private DBUtility(DatabaseReference db_){
        this.db_ = db_;
    }
    public static DBUtility get(){
        if (instance == null){
            instance = new DBUtility(FirebaseDatabase.getInstance().getReference());
        }
        return instance;
    }

    public DatabaseReference getDb_(){
        return db_;
    }



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
    public void getUser(String username, final MyCallBack<User> callBack) {
        db_.child(USERS).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callBack.onCallBack(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


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
        Query freshestPosts = db_.child(POSTS).limitToFirst(20);
        freshestPosts.addValueEventListener(new ValueEventListener() {
            ArrayList<Post> posts = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Log.e("POSTSDB", "test "+data.getValue(Post.class).getTitle());
                    posts.add(data.getValue(Post.class));
                }
                callBack.onCallBack(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ERROR_TAG, "getPostsFeed:onCancelled", databaseError.toException());
            }
        });

    }
}
