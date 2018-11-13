package ch.epfl.swissteam.services;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Adapter for ChatRelations in {@link OnlineChatFragment}
 *
 * @author Sébastien gachoud
 */
public class ChatRelationAdapter extends RecyclerView.Adapter<ChatRelationAdapter.ChatRelationsViewHolder> implements Observer{

    private ExtendedChatRelationsList relations_;
    private List<ExtendedChatRelation> filteredRelation_;
    private String currentUserId_;

    /**
     * Adapter for a list of chatRelations
     *
     * @param relations the list of chatRelations to be managed by the adapter
     */
    public ChatRelationAdapter(List<ChatRelation> relations, String currentUserId) {
        relations_ = new ExtendedChatRelationsList(relations, currentUserId);
        relations_.addObserver(this);
        filteredRelation_ = relations_.getFilteredRelations();
        currentUserId_ = currentUserId;
    }

    @NonNull
    @Override
    public ChatRelationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatrelation_layout, viewGroup, false);

        return new ChatRelationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatRelationsViewHolder holder, int i) {

        ExtendedChatRelation relation = filteredRelation_.get(holder.getAdapterPosition());
        String oUserName = relation.getOthersName_();

        holder.contactName_.setText(oUserName);
        Picasso.get().load(relation.getOthersImageUrl_()).into(holder.contactImage_);

        holder.parentLayout_.setOnClickListener((view) -> {
            Intent intent = new Intent(holder.itemView.getContext(), ChatRoom.class);
            intent.putExtra(ChatRelation.RELATION_ID_TEXT, relation.getChatRelation_().getId_());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.parentLayout_.setOnLongClickListener((view) -> {
            askToDeleteRelation(view.getContext(), relation, oUserName);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return filteredRelation_.size();
    }

    private void askToDeleteRelation(Context context, ExtendedChatRelation chatRelation, String othersName){
        Resources res = context.getResources();
        Utility.askToDeleteAlertDialog(context, chatRelation.getChatRelation_(), null,
                res.getString(R.string.chat_relation_delete_alert_title) + " " + othersName,
                res.getString(R.string.chat_relation_delete_alert_text),
                (b) -> {
                    if(b){
                        relations_.remove(chatRelation);
                        refresh();
                    }

                });
    }

    @Override
    public void update(Observable observable) {
        refresh();
    }

    public void refresh(){
        filteredRelation_ = relations_.getFilteredRelations();
        notifyDataSetChanged();
    }

    public void setFilterName(String filterName){
        relations_.setFilterName_(filterName);
    }

    /**
     * ViewHolder for ChatRelations
     */
    static class ChatRelationsViewHolder extends RecyclerView.ViewHolder {
        
        protected TextView contactName_;
        protected FrameLayout parentLayout_;
        protected ImageView contactImage_;

        /**
         * Create a ChatRelationsViewHolder
         *
         * @param view the current View
         */
        protected ChatRelationsViewHolder(View view) {
            super(view);
            contactName_ = view.findViewById(R.id.chatrelation_adapter_contact_name_view);
            contactImage_ = view.findViewById(R.id.chatrelation_layout_image);
            parentLayout_ = view.findViewById(R.id.framelayout_chatrelation);
        }
    }

}