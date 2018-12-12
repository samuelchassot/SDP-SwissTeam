package ch.epfl.swissteam.services.view.builders;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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

import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.models.User;
import ch.epfl.swissteam.services.providers.LocationManager;
import ch.epfl.swissteam.services.view.activities.ProfileActivity;

import static ch.epfl.swissteam.services.view.activities.NewProfileDetailsActivity.GOOGLE_ID_TAG;

/**
 * An adapter for User
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> users_;
    private Context context_;
    private Location referenceLocation_;

    /**
     * Create an adapter for User
     *
     * @param users a list of User
     * @param context the current context
     */
    public UserAdapter(ArrayList<User> users, Context context){
        users_ = users;
        context_ = context;
        referenceLocation_ = LocationManager.get().getCurrentLocation_();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_search_layout, viewGroup, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int i) {
        holder.nameView_.setText(users_.get(i).getName_());
        holder.bodyView_.setText(users_.get(i).getDescription_());
        int rating = users_.get(i).getRating_();

        int[] attrs = new int[] {R.attr.star_grey};
        TypedArray ta = context_.obtainStyledAttributes(attrs);

        for (int j = 0; j < 5; j++) {
            if (rating >= User.RATING_[j]){
                holder.starView_[j].setBackgroundResource(R.drawable.star_yellow);
            } else {
                holder.starView_[j].setBackgroundResource(ta.getResourceId(0,0));
            }
        }
        ta.recycle();

        int distance = -1;
        if(referenceLocation_ != null){
            Location userLoc = new Location("");
            userLoc.setLatitude(users_.get(i).getLatitude_());
            userLoc.setLongitude(users_.get(i).getLongitude_());
            distance = (int)userLoc.distanceTo(referenceLocation_)/LocationManager.M_IN_ONE_KM;
        }
        String distanceText = context_.getString(R.string.user_search_distanceunavailable);
        if(distance != -1){
            distanceText = context_.getString(R.string.user_search_distancedisplay, distance);
        }
        holder.distanceView_.setText(distanceText);
        Picasso.get().load(users_.get(i).getImageUrl_()).into(holder.imageView_);

        holder.parentLayout_.setOnClickListener((view) -> {
            Intent intent = new Intent(context_, ProfileActivity.class);
            intent.putExtra(GOOGLE_ID_TAG, users_.get(i).getGoogleId_());
            context_.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users_.size();
    }

    /**
     * ViewHolder for the User
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView nameView_;
        public TextView bodyView_;
        public TextView distanceView_;
        public ImageView imageView_;
        public ImageView starView_[] = new ImageView[5];
        private View parentLayout_;

        /**
         * Create a UserViewHolder
         *
         * @param v the current View
         */
        public UserViewHolder(View v) {
            super(v);
            nameView_ = (TextView) v.findViewById(R.id.textview_usersearchlayout_name);
            bodyView_ = (TextView) v.findViewById(R.id.textview_usersearchlayout_body);
            imageView_ = v.findViewById(R.id.imageview_usersearchlayout_image);
            distanceView_ = v.findViewById(R.id.textview_usersearchlayout_distance);
            parentLayout_ = v.findViewById(R.id.parent_layout);
            starView_[0] = v.findViewById(R.id.imageview_usersearchlayout_star0);
            starView_[1] = v.findViewById(R.id.imageview_usersearchlayout_star1);
            starView_[2] = v.findViewById(R.id.imageview_usersearchlayout_star2);
            starView_[3] = v.findViewById(R.id.imageview_usersearchlayout_star3);
            starView_[4] = v.findViewById(R.id.imageview_usersearchlayout_star4);
        }
    }
}
