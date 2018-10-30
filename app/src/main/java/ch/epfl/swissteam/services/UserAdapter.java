package ch.epfl.swissteam.services;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> users_;
    private Context context_;
    private Location referenceLocation_;

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public TextView nameView_;
        public TextView bodyView_;
        public TextView ratingView_;
        public TextView distanceView_;
        public ImageView imageView_;
        private View parentLayout;

        public UserViewHolder(View v) {
            super(v);
            nameView_ = (TextView) v.findViewById(R.id.textview_usersearchlayout_name);
            bodyView_ = (TextView) v.findViewById(R.id.textview_usersearchlayout_body);
            imageView_ = v.findViewById(R.id.imageview_usersearchlayout_image);
            ratingView_ = v.findViewById(R.id.textview_usersearchlayout_rating);
            distanceView_ = v.findViewById(R.id.textview_usersearchlayout_distance);
            parentLayout = v.findViewById(R.id.parent_layout);
        }
    }

    public UserAdapter(ArrayList<User> users, Context context){
        users_ = users;
        context_ = context;
        referenceLocation_ = LocationManager.get().getCurrentLocation_();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_search_layout,viewGroup, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int i) {
        holder.nameView_.setText(users_.get(i).getName_());
        holder.bodyView_.setText(users_.get(i).getDescription_());
        holder.ratingView_.setText(Integer.toString(users_.get(i).getRating_()));
        int distance = -1;
        if(referenceLocation_ != null){
            Location userLoc = new Location("");
            userLoc.setLatitude(users_.get(i).getLatitude_());
            userLoc.setLongitude(users_.get(i).getLongitude_());
            distance = (int)userLoc.distanceTo(referenceLocation_)/LocationManager.M_IN_ONE_KM;
        }
        String distanceText = context_.getString(R.string.usersearch_distanceunavailable);
        if(distance != -1){
            distanceText = context_.getString(R.string.usersearch_distancedisplay, distance);
        }
        holder.distanceView_.setText(distanceText);
        Picasso.get().load(users_.get(i).getImageUrl_()).into(holder.imageView_);

        holder.parentLayout.setOnClickListener((view) -> {
            Intent intent = new Intent(context_, ProfileActivity.class);
            intent.putExtra(GOOGLE_ID_TAG, users_.get(i).getGoogleId_());
            context_.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users_.size();
    }
}
