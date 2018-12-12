package ch.epfl.swissteam.services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static ch.epfl.swissteam.services.PostAdapter.POST_TAG;

/**
 * Adapter for TodoList
 *
 * @author Ghali Chraïbi
 */
public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    private List<Post> todoPosts_;
    private String id_;

    /**
     * Adapter for a list of Posts to do
     *
     * @param posts the list of Posts to do, to be managed by the adapter
     */
    public TodoListAdapter(List<Post> posts) {
        this.todoPosts_ = posts;
        id_ = GoogleSignInSingleton.get().getClientUniqueID();
    }

    @NonNull
    @Override
    public TodoListAdapter.TodoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todolist_layout, viewGroup, false);
        return new TodoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int i) {
        holder.titleView_.setText(todoPosts_.get(holder.getAdapterPosition()).getTitle_());
        holder.bodyView_.setText(todoPosts_.get(holder.getAdapterPosition()).getBody_());

        holder.doneCheckBox_.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                TodoListDbHelper todoListDbHelper = new TodoListDbHelper(buttonView.getContext());
                TodoListDBUtility.deletePost(todoListDbHelper, id_,todoPosts_.get(holder.getAdapterPosition()).getKey_());
                todoPosts_.remove(holder.getAdapterPosition());
                ((RecyclerView) buttonView.getParent().getParent().getParent().getParent().getParent().getParent()).getAdapter().notifyDataSetChanged();

                // when this box is deleted, doesn't check the next one
                buttonView.setChecked(false);
            }
        });

        holder.parentLayout_.setOnClickListener((view) -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostActivity.class);
            intent.putExtra(POST_TAG, todoPosts_.get(holder.getAdapterPosition()));
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return todoPosts_.size();
    }

    /**
     * ViewHolder for list of things to do
     */
    static class TodoViewHolder extends RecyclerView.ViewHolder {

        protected TextView titleView_;
        protected TextView bodyView_;
        protected LinearLayout parentLayout_;
        public CheckBox doneCheckBox_;

        /**
         * Create a TodoViewHolder
         *
         * @param v the current View
         */
        protected TodoViewHolder(View v) {
            super(v);
            titleView_ = v.findViewById(R.id.textview_todolistadapter_title);
            bodyView_ = v.findViewById(R.id.textview_todolistadapter_body);
            doneCheckBox_ = v.findViewById(R.id.checkbox_todolist_layout);
            parentLayout_ = v.findViewById(R.id.linearLayout_todopost);
        }
    }
}
