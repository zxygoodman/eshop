package com.feicui.myeshop.base.wrapper;


import android.app.Activity;
import android.support.v4.app.Fragment;

import com.feicui.myeshop.R;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 下拉刷新和加载的包装类
 */

public abstract class PtrWrapper {

    /**
     * 1. 找到刷新控件：构造方法
     * 2. 设置刷新的头布局样式、尾部加载的布局
     * 3. 刷新的处理，获取数据
     * 4. 自动刷新、延时自动刷新
     * 5. 停止刷新
     * 6. 判断是不是在刷新
     */

    private PtrFrameLayout mFrameLayout;

    public PtrWrapper(Activity activity, boolean isNeedLoad) {
        mFrameLayout = ButterKnife.findById(activity, R.id.standard_refresh_layout);
        initPtr(isNeedLoad);
    }

    public PtrWrapper(Fragment fragment,boolean isNeedLoad){
        mFrameLayout = ButterKnife.findById(fragment.getView(),R.id.standard_refresh_layout);
        initPtr(isNeedLoad);
    }

    // 初始化刷新加载
    private void initPtr(boolean isNeedLoad) {
        if (mFrameLayout != null) {
            mFrameLayout.disableWhenHorizontalMove(true);
        }
        // 设置刷新的布局：头布局
        initPtrHeader();

        // 需要加载，设置尾部布局
        if (isNeedLoad) {
            initPtrFooter();
        }

        mFrameLayout.setPtrHandler(mHandler);
    }

    // 处理刷新和加载
    private PtrDefaultHandler2 mHandler = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            // 加载
            onLoadMore();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            // 刷新
            onRefresh();
        }
    };

    // 设置刷新布局
    private void initPtrHeader() {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mFrameLayout.getContext());
        mFrameLayout.setHeaderView(header);
        mFrameLayout.addPtrUIHandler(header);
    }

    // 设置加载的布局
    private void initPtrFooter() {
        PtrClassicDefaultFooter footer = new PtrClassicDefaultFooter(mFrameLayout.getContext());
        mFrameLayout.setFooterView(footer);
        mFrameLayout.addPtrUIHandler(footer);
    }

    // 自动刷新
    public void autoRefresh() {
        mFrameLayout.autoRefresh();
    }

    // 延时自动刷新
    public void postRefreshDelayed(long delay) {
        mFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFrameLayout.autoRefresh();
            }
        }, delay);
    }

    // 停止刷新
    public void stopRefresh() {
        if (isRefreshing()) {
            mFrameLayout.refreshComplete();
        }
    }

    // 是不是正在刷新
    public boolean isRefreshing() {
        return mFrameLayout.isRefreshing();
    }

    //刷新
    protected abstract void onRefresh();

    //加载更多
    protected abstract void onLoadMore();

}
