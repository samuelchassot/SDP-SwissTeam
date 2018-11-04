package ch.epfl.swissteam.services;


import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Class to represent chatRelation between users.
 *
 * @author Sébastien Gachoud
 */
public class ChatRelation implements DBSavable {

    public static final String RELATION_ID_TEXT = "relationId";


    //Attributes
    private String firstUserId_;
    private String secondUserId_;
    private String id_;

    /**
     * Defines the id of the two users of the relation
     *
     * @param firstUser  the first user in the relation
     * @param secondUser the second user in the relation
     */
    public ChatRelation(User firstUser, User secondUser ) {
        //set users IDs in alphanumeric order
        assignUsers(firstUser, secondUser);
    }

    public ChatRelation(){}

    //Getters

    /**
     * Gives the id of the first user in the relation
     *
     * @return the id of the first user
     */
    public String getFirstUserId_() {
        return firstUserId_;
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
     * Gives the id of the relation
     *
     * @return the id of the relation
     */
    public String getId_() {
        return id_;
    }

    //Setters

    /**
     * Set the id of the first user in the relation.
     *
     * @param firstUserId the id of the first user
     */
    public void setFirstUserId_(String firstUserId) {
        if(firstUserId == null) {
            throw new NullPointerException("ChatRelations requires non null firstUser");
        }

        if(secondUserId_ == null) {firstUserId_ = firstUserId;}
        else {assignUsersId(firstUserId, secondUserId_);} //set users IDs in alphanumeric order
    }

    /**
     * Set the id of the second user in the relation.
     *
     * @param secondUserId the id of the second user
     */
    public void setSecondUserId_(String secondUserId) {
        if(secondUserId == null) {
            throw new NullPointerException("ChatRelations requires non null secondUser");
        }

        if(firstUserId_ == null) {secondUserId_ = secondUserId;}
        else {assignUsersId(firstUserId_, secondUserId);} //set users IDs in alphanumeric order
    }

    /**
     * Set the id of the relation
     *
     * @param id the id of the relation
     */
    public void setId_(String id) {
        this.id_ = id;
    }

    //public methods

    /**
     * assign users by their ID in alphanumeric order, firstUser < secondUser.
     * It does not matter in which order the IDs are input, this method take care of ordering them.
     * @param firstUser a reference to the first user
     * @param secondUser    a reference to the second user
     */
    public void assignUsers(User firstUser, User secondUser) {
        if(firstUser == null) {
            throw new NullPointerException("ChatRelations requires non null firstUser");
        }
        if(secondUser == null) {
            throw new NullPointerException("ChatRelations requires non null secondUser");
        }

        //set users IDs in alphanumeric order
        assignUsersId(firstUser.getGoogleId_(), secondUser.getGoogleId_());
    }

    /**
     * assign users ID in alphanumeric order, firstUser < secondUser.
     * It does not matter in which order the IDs are input, this method take care of ordering them.
     * @param firstId   the id of the first user
     * @param secondId  the id of the second user
     */
    private void assignUsersId(String firstId, String secondId) {
        if(firstId == null) {
            throw new NullPointerException("ChatRelations requires non null firstUser googleId");
        }
        if(secondId == null) {
            throw new NullPointerException("ChatRelations requires non null secondUser googleId");
        }

        if(firstId.compareTo(secondId) <= 0) {
            firstUserId_ = firstId;
            secondUserId_ = secondId;
        }
        else {
            firstUserId_ = secondId;
            secondUserId_ = firstId;
        }
    }

    /**
     * return the id of the user that is in relation with the user with the ID currentUserId
     *
     * @throws IllegalArgumentException if the currentUserId is not an ID in the relation
     * @param currentUserId the ID of the user that needs to know his partner ID
     * @return  the googleID of the other user in the relation
     */
    public String getOtherId(String currentUserId) {
        if(!isInThisRelation(currentUserId)) {
            throw new IllegalArgumentException("The current user does not belong to this ChatRelation");
        }

        if(firstUserId_.compareTo(currentUserId) == 0) {
            return secondUserId_;
        }
        else {
            return firstUserId_;
        }
    }

    /**
     * whether the user with googleID id is part of this relation
     * @param id the googleID of the user
     * @return true if the user is part of this relation, false otherwise
     */
    private Boolean isInThisRelation(String id) {
        return firstUserId_.compareTo(id) == 0 || secondUserId_.compareTo(id) == 0;
    }


    @Override
    public void addToDB(DatabaseReference databaseReference) {
        if(id_ == null) {
            id_ = databaseReference.child(DBUtility.get().CHATS_RELATIONS).push().getKey();
        }
        databaseReference.child(DBUtility.get().CHATS_RELATIONS).child(id_).setValue(this);
    }


}
