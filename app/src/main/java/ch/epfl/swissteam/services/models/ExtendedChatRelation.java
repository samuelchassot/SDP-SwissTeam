package ch.epfl.swissteam.services.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import ch.epfl.swissteam.services.providers.DBCallBack;
import ch.epfl.swissteam.services.providers.DBUtility;

/**
 * This class allows to store chat relations with some extra information such as the name of the
 * chat partner and the timestamp of the last message and therefore provides a better control over
 * the display of the relations. An extended chat relation is designed to be a reactive object over
 * its completion which is achieved when all required information have been fetched from the database.
 * When completed the ExtendedChatRelation will call its ready call back.
 * @author Sébastien Gachoud
 */
public class ExtendedChatRelation {
    private String othersName_;
    private String othersImageUrl_;
    private long timestamp_;
    private final DBCallBack<ExtendedChatRelation> ready_;
    private final ChatRelation chatRelation_;

    /**
     *
     * @param chatRelation  The chatRelation which is supposed to be extended by this
     * @param currentUserId The id of the current user
     * @param ready         The call back to call when this is completed (see class description for more info)
     */
    public ExtendedChatRelation(ChatRelation chatRelation, String currentUserId, DBCallBack<ExtendedChatRelation> ready){
        ready_ = ready;
        chatRelation_ = chatRelation;
        DBUtility.get().getUser(chatRelation_.getOtherId(currentUserId), user ->{
            if(user != null) {
                othersName_ = user.getName_();
                othersImageUrl_ = user.getImageUrl_();
            }
            else{
                othersName_ = User.DELETED_USER_NAME;
                othersImageUrl_ = User.DELETED_USER_IMG_URL;
            }
            retrieveLastTimestamp(timestamp -> {
                timestamp_ = timestamp;
                ready_.onCallBack(this);
            });
        });
    }

    /**
     *
     * @return the name of the chat partner
     */
    public String getOthersName_() {
        return othersName_;
    }

    /**
     *
     * @return the url of the profile pictures of the chat partner
     */
    public String getOthersImageUrl_() {
        return othersImageUrl_;
    }

    /**
     *
     * @return the chat relation that this extends
     */
    public ChatRelation getChatRelation_() {
        return chatRelation_;
    }

    /**
     *
     * @return the timestamp of the last message of this chat relation
     */
    public long getTimestamp_() { return timestamp_; }

    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != ExtendedChatRelation.class) {return false;}
        else{
            return chatRelation_.equals(((ExtendedChatRelation)o).getChatRelation_());
        }
    }

    private void retrieveLastTimestamp(DBCallBack<Long> callBackTimestamp){
        Query lastQuery = DBUtility.get().getDb_()
                .child(DBUtility.CHATS).child(chatRelation_.getId_()).orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                ChatMessage cM = null;
                if(iter.hasNext()) {
                    cM = iter.next().getValue(ChatMessage.class);
                }
                callBackTimestamp.onCallBack(cM == null ? 0 : cM.getTime_());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
    }
}
