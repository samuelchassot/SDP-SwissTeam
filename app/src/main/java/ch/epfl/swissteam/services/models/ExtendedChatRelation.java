package ch.epfl.swissteam.services.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import ch.epfl.swissteam.services.providers.DBCallBack;
import ch.epfl.swissteam.services.providers.DBUtility;

public class ExtendedChatRelation {
    private String othersName_;
    private String othersImageUrl_;
    private long timestamp_;
    private DBCallBack<ExtendedChatRelation> ready_;
    private ChatRelation chatRelation_;

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

    public String getOthersName_() {
        return othersName_;
    }

    public String getOthersImageUrl_() {
        return othersImageUrl_;
    }

    public ChatRelation getChatRelation_() {
        return chatRelation_;
    }

    public long getTimestamp_() { return timestamp_; }

    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != ExtendedChatRelation.class) {return false;}
        else{
            return chatRelation_.equals(((ExtendedChatRelation)o).getChatRelation_());
        }
    }

    public void retrieveLastTimestamp(DBCallBack<Long> callBackTimestamp){
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
