package ch.epfl.swissteam.services;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Utility class that wraps tools related with Firebase database
 */
public class DBUtility {

    public final static String USERS = "Users";
    public final static String CATEGORIES = "Categories";
    public final static String POSTS = "Posts";
    public final static String ERROR_TAG = "DBUtility";
    public final static String CHATS = "Chats";
    public final static String CHATS_RELATIONS = "ChatRelations";
    private final int POSTS_DISPLAY_NUMBER = 100;

    private DatabaseReference db_;
    private static DBUtility instance;
    private boolean isNotificationsSetupDone = false;


    private DBUtility(DatabaseReference db) {
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
     * Get the instance of the Database
     * @return the instance of the database
     */
    public FirebaseDatabase getInstance(){ return FirebaseDatabase.getInstance();};

    /**
     * Get all users' ID for a given category
     *
     * @param category the category
     * @param callBack the CallBack to use
     */
    public void getUsersFromCategory(Categories category, final DBCallBack<ArrayList<String>> callBack) {

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
    public void getUser(String googleId, final DBCallBack<User> callBack) {

        if (googleId == null) {
            User nullUser = null;//new User(null, null, null, null, null, null);
            callBack.onCallBack(nullUser);
            return;
        }
        if(googleId.equals(User.getDeletedUserGoogleID())){
            User deletedUser = User.getDeletedUser();
            callBack.onCallBack(deletedUser);
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
     * Get all users inside the database
     *
     * @param callBack the callBack to use
     */
    public void getAllUsers(final DBCallBack<ArrayList<User>> callBack) {

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

    public void getAllMessagesFromChatRelation(String chatRelationId, final DBCallBack<ArrayList<ChatMessage>> callBack){
        db_.child(DBUtility.CHATS).child(chatRelationId).addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<ChatMessage> messages = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    messages.add(data.getValue(ChatMessage.class));
                }
                callBack.onCallBack(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Retrieves the POSTS_DISPLAY_NUMBER freshest post of the database in geographical range of the user.
     *  @param callBack     the function called on the callBack
     * @param userLocation the location of the user
     * @param helper the helper for the settings local database
     */
    public void getPostsFeed(final DBCallBack<ArrayList<Post>> callBack, Location userLocation, SettingsDbHelper helper) {
        Query freshestPosts = db_.child(POSTS).orderByChild("timestamp_").limitToFirst(POSTS_DISPLAY_NUMBER);
        freshestPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Post> posts = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Post post = data.getValue(Post.class);
                    Location postLocation = new Location("");
                    postLocation.setLongitude(post.getLongitude_());
                    postLocation.setLatitude(post.getLatitude_());
                    int radius = SettingsDBUtility.retrieveRadius(helper, GoogleSignInSingleton.get().getClientUniqueID());
                    if (postLocation.distanceTo(userLocation) <= radius) {
                        posts.add(0, post);
                    }
                }
                callBack.onCallBack(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ERROR_TAG, "getPostsFeed:onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Get Category from DB and execute CallBack
     *
     * @param category Category wanted from DB
     * @param callBack Callback to execute
     */
    public void getCategory(Categories category, final DBCallBack<Categories> callBack) {
        db_.child(CATEGORIES).child(category.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    category.addUser(data.getKey());
                }
                callBack.onCallBack(category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieves all the posts from one user
     *
     * @param googleID the ID of the user
     * @param callBack the function called on the callBack
     */
    public void getUsersPosts(String googleID, final DBCallBack<ArrayList<Post>> callBack) {
        Query usersPosts = db_.child(POSTS).orderByChild("googleId_").equalTo(googleID);
        usersPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Post> posts = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
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

    /**
     * Retrieve a chat relation from the db
     * @param relationId the id of the relation to retrieve
     * @param callBack the callBack containing the instance of the desired relation
     */
    public void getChatRelation(String relationId, final DBCallBack<ChatRelation> callBack){
        if(relationId == null) callBack.onCallBack(null);
        else
        db_.child(DBUtility.CHATS_RELATIONS).child(relationId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callBack.onCallBack(dataSnapshot.getValue(ChatRelation.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Delete a post of a given key
     *
     * @param key the key of the post
     */
    public void deletePost(String key) {
        db_.child(POSTS).child(key).setValue(null);
    }

    /**
     * Add a post to the database
     *
     * @param post post to add
     */
    public void setPost(Post post) {
        db_.child(POSTS).child(post.getKey_()).setValue(post);
    }

    public void notifyNewMessages(Activity activity, String googleId) {
        if (!isNotificationsSetupDone && googleId != null) {
            db_.child(USERS).child(googleId).child("chatRelations_").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot relationDataSnapshot, @Nullable String s) {
                    db_.child(CHATS).child(relationDataSnapshot.getValue(ChatRelation.class).getId_()).addValueEventListener(new ValueEventListener() {
                        private boolean isBound = false;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (isBound) {
                                ChatMessage lastChild = null;
                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                    lastChild = child.getValue(ChatMessage.class);
                                }
                                if(lastChild != null && !lastChild.getUserId_().equals(GoogleSignInSingleton.get().getClientUniqueID())) {
                                    NotificationUtils.sendChatNotification(activity,
                                            "New message!", lastChild.getUser_() + ": " + lastChild.getText_(), lastChild.getRelationId_());
                                }
                            } else {
                                isBound = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot relationDataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot relationDataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot relationDataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        isNotificationsSetupDone = true;
    }
}
