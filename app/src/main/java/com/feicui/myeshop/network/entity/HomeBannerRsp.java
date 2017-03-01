package com.feicui.myeshop.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 首页轮播图接口响应体.
 */
public class HomeBannerRsp {

    @SerializedName("data") private Data mData;

    @SerializedName("status") private Status mStatus;

    public Status getStatus() {
        return mStatus;
    }

    public Data getData() {
        return mData;
    }

    public static class Data {

        // 首页轮播图
        @SerializedName("player") private List<Banner> mBanners;

        // 首页促销商品
        @SerializedName("promote_goods") private List<SimpleGoods> mGoodsList;

        public List<Banner> getBanners() {
            return mBanners;
        }

        public List<SimpleGoods> getGoodsList() {
            return mGoodsList;
        }
    }
}
