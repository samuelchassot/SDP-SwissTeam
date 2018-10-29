package ch.epfl.swissteam.services;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Adapter for categories used in {@link RecyclerView}.
 *
 * @author Adrian Baudat
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private Categories[] capabilities_;

    public CategoriesAdapter(Categories[] capabilities) {
        this.capabilities_ = capabilities;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoriesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capability_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder categoriesViewHolder, int i) {
        categoriesViewHolder.nameView.setText(capabilities_[i].toString());
        addAddListener(categoriesViewHolder.checkBox, capabilities_[i]);
    }

    @Override
    public int getItemCount() {
        return capabilities_.length;
    }

    private void addAddListener(View view, Categories capability) {
        view.setOnClickListener(v -> {
            ((NewProfileCapabilities) v.getContext()).addCapability(capability);
            addRemoveListener(view, capability);
        });
    }

    private void addRemoveListener(View view, Categories capability) {
        view.setOnClickListener(v -> {
            ((NewProfileCapabilities) v.getContext()).removeCapability(capability);
            addAddListener(view, capability);
        });
    }


    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        public TextView nameView;
        public CheckBox checkBox;

        public CategoriesViewHolder(@NonNull View v) {
            super(v);
            this.nameView = v.findViewById(R.id.textview_capabilitylayout_name);
            this.checkBox = v.findViewById(R.id.checkbox_capabilitylayout_check);
        }
    }
}