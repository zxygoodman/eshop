package com.feicui.myeshop.network;

import com.feicui.myeshop.network.entity.Status;
import com.google.gson.annotations.SerializedName;

/**
 * 响应的实体基类：为了防止直接实例化，所以做成抽象类
 */

public class ResponseEntity {

    @SerializedName("status")
    private Status mStatus;

    public Status getStatus() {
        return mStatus;
    }
}
