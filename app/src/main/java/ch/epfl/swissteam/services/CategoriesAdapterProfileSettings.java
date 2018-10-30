package ch.epfl.swissteam.services;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for categories used in the {@link RecyclerView} on the ProfileSettings page.
 *
 * @author Samuel Chassot
 */
public class CategoriesAdapterProfileSettings extends RecyclerView.Adapter<CategoriesAdapterProfileSettings.CategoriesViewHolder> {

    private Categories[] capabilities_;
    private ArrayList<Categories> userCapabilities_;

    /**
     * TODO : Explain
     *
     * @param capabilities
     * @param userCapabilities
     */
    public CategoriesAdapterProfileSettings(Categories[] capabilities, ArrayList<Categories> userCapabilities) {
        this.capabilities_ = capabilities;
        if (userCapabilities != null) {
            this.userCapabilities_ = userCapabilities;
        }
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoriesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capability_layout, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder categoriesViewHolder, int i) {
        categoriesViewHolder.nameView_.setText(capabilities_[i].toString());
        categoriesViewHolder.checkBox_.setChecked(userCapabilities_.contains(capabilities_[i]));
        categoriesViewHolder.checkBox_.setOnClickListener(v -> {
            ((ProfileSettings) v.getContext()).updateUserCapabilities(capabilities_[i], ((CheckBox) v).isChecked());
        });

    }

    @Override
    public int getItemCount() {
        return capabilities_.length;
    }

    /**
     * View Holder for the CategoriesAdapter.
     */
    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        public TextView nameView_;
        public CheckBox checkBox_;

        /**
         * Create a CategoriesViewHolder
         *
         * @param v the current View
         */
        public CategoriesViewHolder(@NonNull View v) {
            super(v);
            this.nameView_ = v.findViewById(R.id.textview_capabilitylayout_name);
            this.checkBox_ = v.findViewById(R.id.checkbox_capabilitylayout_check);
        }
    }
}