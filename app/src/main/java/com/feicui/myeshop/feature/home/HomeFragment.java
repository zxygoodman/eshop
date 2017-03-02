package com.feicui.myeshop.feature.home;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseFragment;
import com.feicui.myeshop.base.widgets.banner.BannerAdapter;
import com.feicui.myeshop.base.widgets.banner.BannerLayout;
import com.feicui.myeshop.base.wrapper.PtrWrapper;
import com.feicui.myeshop.base.wrapper.ToastWrapper;
import com.feicui.myeshop.base.wrapper.ToolbarWrapper;
import com.feicui.myeshop.network.ApiPath;
import com.feicui.myeshop.network.EShopClient;
import com.feicui.myeshop.network.ResponseEntity;
import com.feicui.myeshop.network.UICallback;
import com.feicui.myeshop.network.entity.Banner;
import com.feicui.myeshop.network.entity.HomeBannerRsp;
import com.feicui.myeshop.network.entity.HomeCategoryRsp;
import com.feicui.myeshop.network.entity.Picture;
import com.feicui.myeshop.network.entity.SimpleGoods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 * Created by Administrator on 2017/2/28.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_home_goods)
    ListView mListHomeGoods;
    private BannerAdapter<Banner> mBannerAdapter;
    private ImageView[] mIvPromotes = new ImageView[4];
    private TextView mTvPromoteGoods;
    private HomeGoodsAdapter mGoodsAdapter;
    private PtrWrapper mPtrWrapper;

    private boolean mBannerRefreshed = false;
    private boolean mCategoryRefreshed = false;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {

        //利用toolbar包装类
        new ToolbarWrapper(this).setCustomTitle(R.string.home_title);

        //利用刷新包装类
        mPtrWrapper = new PtrWrapper(this, false) {
            @Override
            protected void onRefresh() {
                mBannerRefreshed = false;
                mCategoryRefreshed = false;
                getHomeData();
            }

            @Override
            protected void onLoadMore() {

            }
        };
        mPtrWrapper.postRefreshDelayed(50);

        // ListView的头布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.partial_home_header, mListHomeGoods, false);

        // 找到头布局里面的控件
        BannerLayout bannerLayout = ButterKnife.findById(view, R.id.layout_banner);
        // 数据和视图的绑定
        mBannerAdapter = new BannerAdapter<Banner>() {
            @Override
            protected void bind(ViewHolder holder, Banner data) {
                // 数据和视图的绑定
                Picasso.with(getContext()).load(data.getPicture().getLarge()).into(holder.mImageView);
            }
        };
        bannerLayout.setAdapter(mBannerAdapter);

        // 促销商品
        mIvPromotes[0] = ButterKnife.findById(view, R.id.image_promote_one);
        mIvPromotes[1] = ButterKnife.findById(view, R.id.image_promote_two);
        mIvPromotes[2] = ButterKnife.findById(view, R.id.image_promote_three);
        mIvPromotes[3] = ButterKnife.findById(view, R.id.image_promote_four);

        // 促销单品的TextView
        mTvPromoteGoods = ButterKnife.findById(view, R.id.text_promote_goods);

        mListHomeGoods.addHeaderView(view);

        // 设置适配器
        mGoodsAdapter = new HomeGoodsAdapter();
        mListHomeGoods.setAdapter(mGoodsAdapter);
    }

    // 去请求刷新数据
    private void getHomeData() {
        // 轮播图和促销单品的数据
        UICallback bannerCallback = new UICallback() {
            @Override
            public void onBusinessResponse(boolean isSucces, ResponseEntity responseEntity) {
                mBannerRefreshed = true;
                if (isSucces) {
                    // 数据拿到了，首先给bannerAdapter,另外是给促销单品
                    HomeBannerRsp bannerRsp = (HomeBannerRsp) responseEntity;
                    mBannerAdapter.reset(bannerRsp.getData().getBanners());
                    //设置促销单品展示
                    setPromoteGoods(bannerRsp.getData().getGoodsList());
                }
                if (mBannerRefreshed && mCategoryRefreshed) {
                    //两个接口都拿到数据之后，停止刷新
                    mPtrWrapper.stopRefresh();
                }
            }
        };

        // 推荐的分类商品
        UICallback categoryCallback = new UICallback() {
            @Override
            public void onBusinessResponse(boolean isSucces, ResponseEntity responseEntity) {
                mCategoryRefreshed = true;
                if (isSucces) {
                    // 拿到了推荐分类商品的数据
                    HomeCategoryRsp categoryRsp = (HomeCategoryRsp) responseEntity;
                    mGoodsAdapter.reset(categoryRsp.getData());
                }
                if (mBannerRefreshed && mCategoryRefreshed) {
                    //两个接口都拿到数据之后，停止刷新
                    mPtrWrapper.stopRefresh();
                }
            }
        };
        EShopClient.getInstance().enqueue(ApiPath.HOME_DATA, null, HomeBannerRsp.class, bannerCallback);
        EShopClient.getInstance().enqueue(ApiPath.HOME_CATEGORY, null, HomeCategoryRsp.class, categoryCallback);
    }

    // 设置促销单品的展示
    private void setPromoteGoods(List<SimpleGoods> goodsList) {
        mTvPromoteGoods.setVisibility(View.VISIBLE);
        for (int i = 0; i < mIvPromotes.length; i++) {
            mIvPromotes[i].setVisibility(View.VISIBLE);
            final SimpleGoods simpleGoods = goodsList.get(i);
            Picture picture = simpleGoods.getImg();

            // 圆形、灰度
            Picasso.with(getContext()).load(picture.getSmall())
                    .transform(new CropCircleTransformation()) //圆形
                    .transform(new GrayscaleTransformation()) //灰度
                    .into(mIvPromotes[i]);

            mIvPromotes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastWrapper.show(simpleGoods.getName());
                }
            });
        }
    }
}
