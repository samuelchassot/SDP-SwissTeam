package ch.epfl.swissteam.services;


import com.google.firebase.database.DatabaseReference;

/**
 * Class to represent chatRelation between users.
 *
 * @author Sébastien Gachoud
 */
public class ChatRelation implements DBSavable {

    public static final String RELATION_ID_TEXT = "relationId";

    private String firstUserId_;
    private String secondUserId_;
    private String id_;

    /**
     * Defines the id of the two users of the relation
     *
     * @param firstUser  the first user in the relation
     * @param secondUser the second user in the relation
     */
    public ChatRelation(User firstUser, User secondUser) {
        this();
        setUsers(firstUser, secondUser);
    }

    /**
     * An empty constructor that defines a null id
     */
    public ChatRelation() {
        id_ = null;
    }

    /**
     * Gives the id of the first user in the relation
     *
     * @return the id of the first user
     */
    public String getFirstUserId_() {
        return firstUserId_;
    }

    /**
     * Set the id of the first user in the relation. If the id of the second
     * user is null, make both users id in the relation be user1Id
     *
     * @param user1Id the id of the first user
     */
    public void setFirstUserId_(String user1Id) {
        if (secondUserId_ == null) {
            setUsersId(user1Id, user1Id);
        } else {
            setUsersId(user1Id, secondUserId_);
        }
    }

    /**
     * Gives the id of the second user in the relation
     *
     * @return the id of the second user
     */
    public String getSecondUserId_() {
        return secondUserId_;
    }

    /**
     * Set the id of the second user in the relation. If the id of the first
     * user is null, make both users id in the relation be user2Id
     *
     * @param user2Id the id of the second user
     */
    public void setSecondUserId_(String user2Id) {
        if (firstUserId_ == null) {
            setUsersId(user2Id, user2Id);
        } else {
            setUsersId(firstUserId_, user2Id);
        }
    }

    /**
     * Gives the id of the relation
     *
     * @return the id of the relation
     */
    public String getId_() {
        return id_;
    }

    /**
     * Set the id of the relation
     *
     * @param id the id of the relation
     */
    public void setId_(String id) {
        this.id_ = id;
    }

    /**
     * Set both users id in the relation
     *
     * @param firstUser  the first user
     * @param secondUser the second user
     */
    public void setUsers(User firstUser, User secondUser) {
        if (firstUser == null) {
            throw new NullPointerException("ChatRelations requires non null firstUser");
        }
        if (secondUser == null) {
            throw new NullPointerException("ChatRelations requires non null secondUser");
        }

        setUsersId(firstUser.getGoogleId_(), secondUser.getGoogleId_());
    }

    /**
     * Set both users id in the relation
     *
     * @param firstId  the id of the first user
     * @param secondId the id of the second user
     */
    private void setUsersId(String firstId, String secondId) {
        if (firstId == null) {
            throw new NullPointerException("ChatRelations requires non null firstUser googleId");
        }
        if (secondId == null) {
            throw new NullPointerException("ChatRelations requires non null secondUser googleId");
        }

        if (firstId.compareTo(secondId) <= 0) {
            firstUserId_ = firstId;
            secondUserId_ = secondId;
        } else {
            firstUserId_ = secondId;
            secondUserId_ = firstId;
        }
    }

    /**
     * Given the id of a user, get the id of the other user in this relation
     *
     * @param currentUserId the id of a user in the relation
     * @return the id of the user related to currentUserId
     */
    public String getOtherId(String currentUserId) {
        if (!isInThisRelation(currentUserId)) {
            throw new IllegalArgumentException("The current user does not belong to this ChatRelation");
        }
        if (firstUserId_.compareTo(currentUserId) == 0) {
            return secondUserId_;
        } else {
            return firstUserId_;
        }
    }

    /**
     * Check if a given id is the id of one of the users in this relation
     *
     * @param id the id to be checked
     * @return true if the id is indeed one of the user's id in the relation, false otherwise
     */
    private Boolean isInThisRelation(String id) {
        return firstUserId_.compareTo(id) == 0 || secondUserId_.compareTo(id) == 0;
    }

    @Override
    public void addToDB(DatabaseReference databaseReference) {
        if (id_ == null) {
            id_ = databaseReference.child(DBUtility.get().CHATS_RELATIONS).push().getKey();
        }
        databaseReference.child(DBUtility.get().CHATS_RELATIONS).child(id_).setValue(this);
    }
}
