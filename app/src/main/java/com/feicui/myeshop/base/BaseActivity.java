package com.feicui.myeshop.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 通用的Activity的基类
 */

public abstract class BaseActivity extends TransitionActivity{

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());
        mUnbinder = ButterKnife.bind(this);

        initView();
    }

    protected abstract void initView();

    // 让子类来告诉我们布局
    @LayoutRes protected abstract int getContentViewLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mUnbinder = null;
    }
}
