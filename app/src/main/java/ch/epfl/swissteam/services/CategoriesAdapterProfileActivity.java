package ch.epfl.swissteam.services;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An adapter for capabilities
 */
public class CategoriesAdapterProfileActivity extends RecyclerView.Adapter<CategoriesAdapterProfileActivity.CategoriesViewHolder> {

    private List<Categories> categories_;
    private Map<String, List<String>> keyWords_;

    /**
     * Create an adapter for capabilities
     *
     * @param capabilities a list of capabilities
     */
    public CategoriesAdapterProfileActivity(List<Categories> capabilities, Map<String, List<String>> keyWords) {
        if(keyWords==null){
            keyWords_ = new HashMap<>();
        }else{
            keyWords_ = keyWords;
        }
        if(capabilities == null) {
            categories_ = new ArrayList<>();
        }else{
            categories_ = capabilities;
        }

    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.capabilities_display_adapter, parent, false);

        CategoriesViewHolder vh = new CategoriesViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder mViewHolder, int position) {
        mViewHolder.mCategoriesName_.setText(categories_.get(position).toString());
        StringBuilder builder = new StringBuilder();
        for (String kw : keyWords_.get(categories_.get(position).toString())){
            builder.append("#").append(kw).append(" ");
        }
        mViewHolder.mKeywordsEditText_.setText(builder.toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categories_ == null ? 0 : categories_.size();
    }

    /**
     * ViewHolder for CategoriesAdapterProfileActivity
     */
    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        public TextView mCategoriesName_;
        public TextView mKeywordsEditText_;

        /**
         * Create a CategoriesAdapterProfileActivity
         *
         * @param v the current View
         */
        public CategoriesViewHolder(View v) {
            super(v);
            mCategoriesName_ = (TextView) v.findViewById(R.id.textview_capabilitiesadapter_capabilityname);
            mKeywordsEditText_ = (TextView) v.findViewById(R.id.textview_capabilitiesadapter_keywords);

        }
    }

}
