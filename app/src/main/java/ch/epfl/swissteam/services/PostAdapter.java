package ch.epfl.swissteam.services;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for Posts in {@link HomeFragment}
 *
 * @author Julie Giunta
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public static final String POST_TAG = "ch.epfl.swissteam.services.post";

    private List<Post> posts_;

    /**
     * Adapter for a list of Posts
     *
     * @param posts the list of Posts to be managed by the adapter
     */
    public PostAdapter(List<Post> posts) {
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
        holder.titleView_.setText(posts_.get(holder.getAdapterPosition()).getTitle_());
        holder.bodyView_.setText(posts_.get(holder.getAdapterPosition()).getBody_());

        DBUtility.get().getUser(posts_.get(holder.getAdapterPosition()).getGoogleId_(), user -> {
            if (user != null) {
                Picasso.get().load(user.getImageUrl_()).into(holder.imageView_);
            }
        });

        Location postLocation = new Location("");
        postLocation.setLongitude(posts_.get(holder.getAdapterPosition()).getLongitude_());
        postLocation.setLatitude(posts_.get(holder.getAdapterPosition()).getLatitude_());

        Location userLocation = LocationManager.get().getCurrentLocation_();

        if (userLocation != null) {
            float distance = postLocation.distanceTo(userLocation) / LocationManager.M_IN_ONE_KM;
            holder.distanceView_.setText(holder.parentLayout_.getContext().getResources().getString(R.string.homefragment_postdistance, distance));
        } else {
            holder.distanceView_.setText(holder.parentLayout_.getContext().getResources().getString(R.string.homefragment_postdistance, LocationManager.MAX_POST_DISTANCE / LocationManager.M_IN_ONE_KM));
        }

        holder.parentLayout_.setOnClickListener((view) -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostActivity.class);
            intent.putExtra(POST_TAG, posts_.get(holder.getAdapterPosition()));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts_.size();
    }

    /**
     * ViewHolder for Posts
     */
    static class PostViewHolder extends RecyclerView.ViewHolder {

        protected TextView titleView_;
        protected TextView bodyView_;
        protected TextView distanceView_;
        protected ImageView imageView_;
        protected FrameLayout parentLayout_;

        /**
         * Create a PostViewHolder
         *
         * @param v the current View
         */
        protected PostViewHolder(View v) {
            super(v);
            titleView_ = v.findViewById(R.id.textview_postadapter_title);
            bodyView_ = v.findViewById(R.id.textview_postadapter_body);
            distanceView_ = v.findViewById(R.id.textview_postadapter_distance);
            imageView_ = v.findViewById(R.id.imageview_postadapter_image);
            parentLayout_ = v.findViewById(R.id.framelayout_post);
        }
    }
}

