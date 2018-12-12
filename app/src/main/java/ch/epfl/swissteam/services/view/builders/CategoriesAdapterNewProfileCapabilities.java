package ch.epfl.swissteam.services.view.builders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import ch.epfl.swissteam.services.R;
import ch.epfl.swissteam.services.models.Categories;
import ch.epfl.swissteam.services.view.activities.NewProfileCapabilitiesActivity;

/**
 * Adapter for categories used in {@link RecyclerView}.
 *
 * @author Adrian Baudat
 */
public class CategoriesAdapterNewProfileCapabilities extends RecyclerView.Adapter<CategoriesAdapterNewProfileCapabilities.CategoriesViewHolder> {

    private Categories[] capabilities_;

    /**
     * Creates a new CategoriesAdapterNewProfileCapabilities from an array of Categories.
     *
     * @param capabilities Array of categories to create from
     */
    public CategoriesAdapterNewProfileCapabilities(Categories[] capabilities) {
        this.capabilities_ = capabilities;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoriesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.capability_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder categoriesViewHolder, int i) {
        categoriesViewHolder.nameView_.setText(capabilities_[i].toString());
        addAddListener(categoriesViewHolder.checkBox_, capabilities_[i], categoriesViewHolder);
        addKeyWordsListener(categoriesViewHolder.keywordsInput_, capabilities_[i], categoriesViewHolder);

    }

    @Override
    public int getItemCount() {
        return capabilities_.length;
    }

    private void addKeyWordsListener(EditText edittext, Categories capability, CategoriesViewHolder holder){

        edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((NewProfileCapabilitiesActivity) edittext.getContext()).addKeyWords(capability, s.toString());
                if (!holder.checkBox_.isChecked()){
                    holder.checkBox_.setChecked(true);
                }
            }
        });
    }

    private void addAddListener(CheckBox view, Categories capability, CategoriesViewHolder holder) {
        view.setOnCheckedChangeListener((v, isChecked) -> {
            ((NewProfileCapabilitiesActivity) v.getContext()).addCapability(capability);
            if(!isChecked) {
                holder.keywordsInput_.setText("");
                view.setChecked(false);
            }
            addRemoveListener(view, capability, holder);
        });
    }

    private void addRemoveListener(CheckBox view, Categories capability, CategoriesViewHolder holder) {
        view.setOnCheckedChangeListener((v, isChecked) -> {
            ((NewProfileCapabilitiesActivity) v.getContext()).removeCapability(capability);
            ((NewProfileCapabilitiesActivity) v.getContext()).removeKeyWords(capability);
            if(!isChecked) {
                holder.keywordsInput_.setText("");
                view.setChecked(false);
            }
            addAddListener(view, capability, holder);
        });
    }

    /**
     * View Holder for the CategoriesAdapter
     */
    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        public TextView nameView_;
        public CheckBox checkBox_;
        public EditText keywordsInput_;

        /**
         * Create a CategoriesViewHolder
         *
         * @param v the current View
         */
        public CategoriesViewHolder(@NonNull View v) {
            super(v);
            this.nameView_ = v.findViewById(R.id.textview_capabilitylayout_name);
            this.checkBox_ = v.findViewById(R.id.checkbox_capabilitylayout_check);
            this.keywordsInput_ = v.findViewById(R.id.edittext_capabilitylayout_keywords);
        }
    }
}