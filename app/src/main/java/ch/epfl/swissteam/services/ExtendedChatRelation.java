package ch.epfl.swissteam.services;

public class ExtendedChatRelation {
    private String othersName_;
    private String othersImageUrl_;
    private DBCallBack<ExtendedChatRelation> ready_;
    private ChatRelation chatRelation_;

    public ExtendedChatRelation(ChatRelation chatRelation, String currentUserId, DBCallBack<ExtendedChatRelation> ready){
        ready_ = ready;
        chatRelation_ = chatRelation;
        DBUtility.get().getUser(chatRelation_.getOtherId(currentUserId),user ->{
            othersName_ = user.getName_();
            othersImageUrl_ = user.getImageUrl_();
            ready_.onCallBack(this);
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

    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != ExtendedChatRelation.class) {return false;}
        else{
            return chatRelation_.equals(((ExtendedChatRelation)o).getChatRelation_());
        }
    }
}
