package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * Adapter for Posts in {@link MyPostsFragment}
 *
 * @author Julie Giunta
 */
public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.PostViewHolder> {

    public static final String POST_TAG = "ch.epfl.swissteam.services.post";

    private List<Post> posts_;

    /**
     * ViewHolder for Posts, with hidden buttons
     */
    static class PostViewHolder extends RecyclerView.ViewHolder{
        protected TextView postView_;
        protected ImageButton editButton_;
        protected ImageButton deleteButton_;

        /**
         * Create a PostViewHolder
         * @param v the current View
         */
        protected PostViewHolder(View v){
            super(v);
            postView_ = v.findViewById(R.id.textview_mypostadapter_post);
            editButton_ = v.findViewById(R.id.button_mypostadapter_edit);
            deleteButton_= v.findViewById(R.id.button_mypostadapter_delete);
        }
    }

    /**
     * Adapter for a list of Posts
     * @param posts the list of Posts to be managed by the adapter
     */
    public MyPostAdapter(List<Post> posts){
        this.posts_ = posts;
    }

    @NonNull
    @Override
    public MyPostAdapter.PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mypost_layout, viewGroup, false);

        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int i) {
        holder.postView_.setText(posts_.get(holder.getAdapterPosition()).getTitle_()
                + "\n" + posts_.get(holder.getAdapterPosition()).getBody_());
        holder.editButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "EDIT CLICKED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), MyPostEdit.class);
                intent.putExtra(POST_TAG, posts_.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
                ((RecyclerView)v.getParent().getParent().getParent().getParent()).getAdapter().notifyDataSetChanged();
            }
        });

        holder.deleteButton_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "DELETE CLICKED", Toast.LENGTH_SHORT).show();
                DBUtility.get().deletePost(posts_.get(holder.getAdapterPosition()).getKey_());
                posts_.remove(holder.getAdapterPosition());
                ((RecyclerView)v.getParent().getParent().getParent().getParent()).getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts_.size();
    }
}
