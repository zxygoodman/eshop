package com.feicui.myeshop.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 首页商品分类接口响应体.
 */
public class HomeCategoryRsp {

    @SerializedName("status") private Status mStatus;

    @SerializedName("data") private List<CategoryHome> mData;

    public List<CategoryHome> getData() {
        return mData;
    }

    public Status getStatus() {
        return mStatus;
    }
}
