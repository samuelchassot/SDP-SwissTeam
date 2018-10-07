package ch.epfl.swissteam.services;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> users_;

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView nameView_;
        public TextView surnameView_;
        public TextView bodyView_;
        public UserViewHolder(View v){
            super(v);
            nameView_ = (TextView) v.findViewById(R.id.nameView);
            bodyView_ = (TextView) v.findViewById(R.id.bodyView);
        }
    }

    public UserAdapter(ArrayList<User> users){
        this.users_ = users;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_search_layout,viewGroup, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int i) {
        holder.nameView_.setText(users_.get(i).getName_() + " " + users_.get(i).getSurname_());
        holder.bodyView_.setText(users_.get(i).getDescription_());
    }

    @Override
    public int getItemCount() {
        return users_.size();
    }
}
