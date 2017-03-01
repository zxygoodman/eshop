package com.feicui.myeshop.feature.category;

import android.view.View;
import android.widget.TextView;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseListAdapter;
import com.feicui.myeshop.network.entity.CategoryPrimary;

import butterknife.BindView;

/**
 * 一级分类的适配器
 */

public class CategoryAdapter extends BaseListAdapter<CategoryPrimary,CategoryAdapter.ViewHolder> {

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_primary_category;
    }

    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.text_category)
        TextView mTextCategory;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void bind(int position) {
            mTextCategory.setText(getItem(position).getName());
        }
    }
}
