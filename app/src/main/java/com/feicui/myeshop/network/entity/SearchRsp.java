package com.feicui.myeshop.network.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;

// 搜索商品的响应体
public class SearchRsp {

    @SerializedName("status") private Status mStatus;

    @SerializedName("data") private List<SimpleGoods> mData;

    @SerializedName("paginated") private Paginated mPaginated;

    public List<SimpleGoods> getData() {
        return mData;
    }

    public Paginated getPaginated() {
        return mPaginated;
    }

    public Status getStatus() {
        return mStatus;
    }
}
