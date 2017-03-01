package com.feicui.myeshop.base.widgets.banner;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feicui.myeshop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * banner的适配器：为了更加的广泛应用，我们可以写成通用的
 * 数据是不是不确定的，但是视图是确定
 */

public abstract class BannerAdapter<T> extends PagerAdapter{

    private List<T> mData = new ArrayList<>();

    public void reset(List<T> data){
        mData.clear();
        if (data != null) mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        ViewHolder viewHolder = (ViewHolder) object;
        return view == viewHolder.itemView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_banner,container,false);
        container.addView(view);
        ViewHolder viewHolder = new ViewHolder(view);

        // 绑定视图和数据
        bind(viewHolder,mData.get(position));
        return viewHolder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewHolder viewHolder = (ViewHolder) object;
        container.removeView(viewHolder.itemView);
    }

    public static class ViewHolder{

        @BindView(R.id.image_banner_item)
        public ImageView mImageView;
        private View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }

    protected abstract void bind(ViewHolder holder,T data);
}
