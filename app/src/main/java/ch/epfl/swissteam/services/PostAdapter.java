package ch.epfl.swissteam.services;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts_;

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        public TextView titleView_;
        public TextView bodyView_;
        public PostViewHolder(View v){
            super(v);
            titleView_ = v.findViewById(R.id.textview_postadapter_title);
            bodyView_ = v.findViewById(R.id.textview_postadapter_body);
        }
    }

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
        holder.titleView_.setText(posts_.get(i).getTitle_());
        holder.bodyView_.setText(posts_.get(i).getBody_());
    }

    @Override
    public int getItemCount() {
        return posts_.size();
    }
}
