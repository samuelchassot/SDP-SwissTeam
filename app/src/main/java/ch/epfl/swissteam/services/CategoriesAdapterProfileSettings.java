package ch.epfl.swissteam.services;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for categories used in the {@link RecyclerView} on the ProfileSettings page.
 *
 * @author Samuel Chassot
 */
public class CategoriesAdapterProfileSettings extends RecyclerView.Adapter<CategoriesAdapterProfileSettings.CategoriesViewHolder> {

    private Categories[] capabilities_;
    private ArrayList<Categories> userCapabilities_;

    public  CategoriesAdapterProfileSettings(Categories[] capabilities, ArrayList<Categories> userCapabilities) {
        this.capabilities_ = capabilities;
        if(userCapabilities != null){
            this.userCapabilities_ = userCapabilities;
        }

    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return  new CategoriesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capability_layout, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder categoriesViewHolder, int i) {
        categoriesViewHolder.nameView.setText(capabilities_[i].toString());
        categoriesViewHolder.checkBox.setChecked(userCapabilities_.contains(capabilities_[i]));
        categoriesViewHolder.checkBox.setOnClickListener(v -> {
            ((ProfileSettings) v.getContext()).updateUserCapabilities(capabilities_[i], ((CheckBox)v).isChecked());
        });

    }

    @Override
    public int getItemCount() {
        return capabilities_.length;
    }

    public static class CategoriesViewHolder extends  RecyclerView.ViewHolder {

        public TextView nameView;
        public CheckBox checkBox;

        public CategoriesViewHolder(@NonNull View v) {
            super(v);
            this.nameView = v.findViewById(R.id.textview_capabilitylayout_name);
            this.checkBox = v.findViewById(R.id.checkbox_capabilitylayout_check);
        }
    }
}