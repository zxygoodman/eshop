package com.feicui.myeshop;

import android.app.Application;

import com.feicui.myeshop.base.wrapper.ToastWrapper;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2017/2/22.
 */

public class EShopApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            // 这个是用于分析内存的线程，我们不能在这里面初始化我们项目
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        // 正常的app初始化

        // Toast的包装类的初始化
        ToastWrapper.init(this);
    }
}
