package ch.epfl.swissteam.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static ch.epfl.swissteam.services.NewProfileDetails.GOOGLE_ID_TAG;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> users_;
    private Context context_;

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public TextView nameView_;
        public TextView surnameView_;
        public TextView bodyView_;
        LinearLayout parentLayout;

        public UserViewHolder(View v) {
            super(v);
            nameView_ = (TextView) v.findViewById(R.id.nameView);
            bodyView_ = (TextView) v.findViewById(R.id.bodyView);
            parentLayout = v.findViewById(R.id.parent_layout);
        }
    }

    public UserAdapter(ArrayList<User> users, Context context){
        users_ = users;
        context_ = context;
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
