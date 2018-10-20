package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRelationAdapter extends RecyclerView.Adapter<ChatRelationAdapter.ChatRelationsViewHolder> {


        private List<ChatRelation> relations_;

        /**
         * ViewHolder for ChatRelations
         */
        static class ChatRelationsViewHolder extends RecyclerView.ViewHolder{
            protected TextView contactName_;
            protected FrameLayout parentLayout_;

            /**
             * Create a ChatRelatinsViewHolder
             * @param view the current View
             */
            protected ChatRelationsViewHolder(View view){
                super(view);
                contactName_ = view.findViewById(R.id.chatrelation_adapter_contact_name_view);
                parentLayout_ = view.findViewById(R.id.framelayout_chatrelation);
            }
        }

        /**
         * Adapter for a list of chatRelations
         * @param relations the list of chatRelations to be managed by the adapter
         */
        public ChatRelationAdapter(List<ChatRelation> relations){
            relations_ = relations == null ? new ArrayList<>() : relations;
        }

        @NonNull
        @Override
        public ChatRelationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatrelation_layout, viewGroup, false);

            return new ChatRelationsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatRelationsViewHolder holder, int i) {
            holder.contactName_.setText(relations_.get(holder.getAdapterPosition()).getFirstUserId_());

            holder.parentLayout_.setOnClickListener((view) -> {
                Intent intent = new Intent(holder.itemView.getContext(), PostActivity.class);
                intent.putExtra(ChatRelation.RELATION_ID_TEXT, relations_.get(holder.getAdapterPosition()).getId_());
                holder.itemView.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return relations_.size();
        }
    }
