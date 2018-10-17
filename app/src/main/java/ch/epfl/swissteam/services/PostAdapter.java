package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public static final String POST_TAG = "ch.epfl.swissteam.services.post";

    private List<Post> posts_;

    /**
     * ViewHolder for Posts
     */
    static class PostViewHolder extends RecyclerView.ViewHolder{
        protected TextView postView_;

        /**
         * Create a PostViewHolder
         * @param v the current View
         */
        protected PostViewHolder(View v){
            super(v);
        }
    }

    /**
     * Adapter for a list of Posts
     * @param posts the list of Posts to be managed by the adapter
     */
    public PostAdapter(List<Post> posts){
        this.posts_ = posts;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_layout, viewGroup, false);

        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int i) {
    }

    @Override
    public int getItemCount() {
        return posts_.size();
    }
}

