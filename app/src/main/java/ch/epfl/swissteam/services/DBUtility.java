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
import java.util.List;

public class DBUtility {

    private DatabaseReference db_;
    private static DBUtility instance;
    private User currentUser_;



    public final static String USERS = "Users";
    public final static String CATEGORIES = "Categories";
    public final static String POSTS = "Posts";
    public final static String ERROR_TAG = "DBUtility";
    public final static String CHATS = "Chats";
    public final static String CHATS_RELATIONS = "ChatRelations";
    private final int POSTS_DISPLAY_NUMBER = 20;


    private DBUtility(DatabaseReference db){
        currentUser_ = null;
        this.db_ = db;
    }

    /**
     * Get the DBUtility instance
     *
     * @return the DBUtility instance
     */
    public static DBUtility get() {
        if (instance == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().getReference().keepSynced(true);
            instance = new DBUtility(FirebaseDatabase.getInstance().getReference());
        }
        return instance;
    }

    /**
     * Get the DatabaseReference of the DBUtility
     *
     * @return the DatabaseReference of the DBUtility
     */
    public DatabaseReference getDb_() {
        return db_;
    }


    /**
     * Get all users' ID for a given category
     *
     * @param category the category
     * @param callBack the CallBack to use
     */
    public void getUsersFromCategory(Categories category, final MyCallBack<ArrayList<String>> callBack) {
        if (category == Categories.ALL) {
            Log.e("DBUtility", "Cannot retrieve all users that way");
        } else {
            db_.child(CATEGORIES).child(category.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
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

    }

    /**
     * Get a user whose ID correspond to googleId
     *
     * @param googleId unique user'Id
     * @param callBack the CallBack to use
     */
    public void getUser(String googleId, final MyCallBack<User> callBack) {


        if (googleId == null) {
            User nullUser = new User(null, null, null, null, null, null);
            callBack.onCallBack(nullUser);
            return;
        }
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
     *
     * @return the current logged user which is null if the db has not yet provided the user
     */
    public User getCurrentUser_(){
        String googleId = GoogleSignInSingleton.get().getClientUniqueID();
        if(currentUser_ == null || currentUser_.getGoogleId_().compareTo(googleId) != 0) {
            currentUser_ = null;
            try{
                getUser(googleId, new MyCallBack<User>() {
                    @Override
                    public void onCallBack(User value) {
                        if(value != null){
                            currentUser_ = value;
                        }
                    }
                });
            }
            catch (NullPointerException e){
                currentUser_ = null;
            }

        }
        return currentUser_;
    }

    /**
     * Get all users inside the database
     *
     * @param callBack the callBack to use
     */
    public void getAllUsers(final MyCallBack<ArrayList<User>> callBack) {

        db_.child(USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<User> users = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
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
     * Retrieves the POSTS_DISPLAY_NUMBER freshest post of the database
     *
     * @param callBack the function called on the callBack
     */
    public void getPostsFeed(final MyCallBack<ArrayList<Post>> callBack) {
        Query freshestPosts = db_.child(POSTS).orderByChild("timestamp_");
        freshestPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Post> posts = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Post post = data.getValue(Post.class);
                    posts.add(0, post);
                }
                callBack.onCallBack(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ERROR_TAG, "getPostsFeed:onCancelled", databaseError.toException());
            }
        });
    }

    public void setUser(User user) {
        db_.child(USERS).child(user.getGoogleId_()).setValue(user);

    }

    public void getCategory(String category, final MyCallBack<Void> callBack) {
        db_.child(CATEGORIES).child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Categories.fromString(category).addUser(data.getKey());
                }
                callBack.onCallBack(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieves all the posts from one user
     * @param googleID the ID of the user
     * @param callBack the function called on the callBack
     */
    public void getUsersPosts(String googleID, final MyCallBack<ArrayList<Post>> callBack){
        Query usersPosts = db_.child(POSTS).orderByChild("googleId_").equalTo(googleID);
        usersPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Post> posts = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Post post = data.getValue(Post.class);
                    posts.add(post);
                }
                callBack.onCallBack(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ERROR_TAG, "getUsersPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void deletePost(String key){
        db_.child(POSTS).child(key).setValue(null);
    }

    public void setPost(Post post){
        db_.child(POSTS).child(post.getKey_()).setValue(post);
    }
}
