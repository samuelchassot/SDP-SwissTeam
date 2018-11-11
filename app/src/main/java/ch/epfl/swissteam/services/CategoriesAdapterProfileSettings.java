package ch.epfl.swissteam.services;

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

import java.util.ArrayList;
import java.util.Map;

/**
 * Adapter for categories used in the {@link RecyclerView} on the ProfileSettings page.
 *
 * @author Samuel Chassot
 */
public class CategoriesAdapterProfileSettings extends RecyclerView.Adapter<CategoriesAdapterProfileSettings.CategoriesViewHolder> {

    private Categories[] capabilities_;
    private ArrayList<Categories> userCapabilities_;
    private Map<String, ArrayList<String>> keyWords_;

    /**
     * Creates an adapter for categories to be used in ProfileSettings
     *
     * @param capabilities the array of categories to be adapted
     * @param userCapabilities a list of capabilities (of the user) to be checked
     */
    public CategoriesAdapterProfileSettings(Categories[] capabilities, ArrayList<Categories> userCapabilities, Map<String, ArrayList<String>> keyWords) {
        this.capabilities_ = capabilities;
        if (userCapabilities != null) {
            this.userCapabilities_ = userCapabilities;
        }
        if(keyWords != null){
            this.keyWords_ = keyWords;
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
        StringBuilder builder = new StringBuilder();
        if(keyWords_.get(capabilities_[i].toString()) != null) {
            for (String kw : keyWords_.get(capabilities_[i].toString())) {
                builder.append(kw).append(";");
            }
            if (builder.length() > 2) {
                builder.delete(builder.length() - 1, builder.length());
            }
            categoriesViewHolder.keyWords_.setText(builder.toString());
        }
        addKeyWordsListener(categoriesViewHolder.keyWords_, capabilities_[i]);

    }

    private void addKeyWordsListener(EditText edittext, Categories capability){
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

                ((ProfileSettings) edittext.getContext()).addKeyWords(capability, s.toString());
            }
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
        public EditText keyWords_;

        /**
         * Create a CategoriesViewHolder
         *
         * @param v the current View
         */
        public CategoriesViewHolder(@NonNull View v) {
            super(v);
            this.nameView_ = v.findViewById(R.id.textview_capabilitylayout_name);
            this.checkBox_ = v.findViewById(R.id.checkbox_capabilitylayout_check);
            this.keyWords_ = v.findViewById(R.id.edittext_capabilitylayout_keywords);
        }
    }
}