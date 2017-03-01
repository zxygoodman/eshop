package com.feicui.myeshop.feature.category;

import android.view.View;
import android.widget.TextView;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseListAdapter;
import com.feicui.myeshop.network.entity.CategoryBase;

import butterknife.BindView;

/**
 * // 子分类的适配器
 */

public class ChildrenAdapter extends BaseListAdapter<CategoryBase, ChildrenAdapter.ViewHolder> {

    @Override
    protected int getItemViewLayout() {
        return R.layout.item_children_category;
    }

    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        @BindView(R.id.text_category)
        TextView mTextCategory;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void bind(int position) {
            mTextCategory.setText(getItem(position).getName());
        }
    }
}
