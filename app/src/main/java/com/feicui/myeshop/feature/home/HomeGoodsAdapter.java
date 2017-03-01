package com.feicui.myeshop.feature.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseListAdapter;
import com.feicui.myeshop.base.wrapper.ToastWrapper;
import com.feicui.myeshop.network.entity.CategoryHome;
import com.feicui.myeshop.network.entity.Picture;
import com.feicui.myeshop.network.entity.SimpleGoods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 首页：推荐商品的适配器
 */

public class HomeGoodsAdapter extends BaseListAdapter<CategoryHome,HomeGoodsAdapter.ViewHolder>{


    @Override
    protected int getItemViewLayout() {
        return R.layout.item_home_goods;
    }

    @Override
    protected ViewHolder getItemViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder{

        @BindView(R.id.text_category)
        TextView mTvcategory;
        @BindViews({
                R.id.image_goods_01,
                R.id.image_goods_02,
                R.id.image_goods_03,
                R.id.image_goods_04})
        ImageView[] mImageViews;
        private CategoryHome mCategoryHome;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(int position) {
            mCategoryHome = getItem(position);
            mTvcategory.setText(mCategoryHome.getName());
            final List<SimpleGoods> hotGoodsList = mCategoryHome.getHotGoodsList();
            for (int i = 0; i < mImageViews.length; i++) {
                // 取出商品List里面的商品图片
                Picture picture = hotGoodsList.get(i).getImg();
                // Picasso加载图片
                Picasso.with(getContext()).load(picture.getLarge()).into(mImageViews[i]);

                final int index = i;
                // 设置点击事件
                mImageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleGoods simpleGoods = hotGoodsList.get(index);
                        ToastWrapper.show(simpleGoods.getName());
                    }
                });
            }
        }
        @OnClick(R.id.text_category)
        void onClick(){
            ToastWrapper.show(mCategoryHome.getName());
        }
    }

}
