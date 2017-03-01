package com.feicui.myeshop.base.wrapper;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseActivity;
import com.feicui.myeshop.base.BaseFragment;

import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

/**
 * Toolbar的包装类
 */

public class ToolbarWrapper {

    /**
     * 1. 根据id找到Toolbar控件，如果Toolbar里面还包括文本，文本的控件也要绑定
     * 2. 设置Toobar为ActionBar
     * 3. 设置标题：隐藏默认的标题，展示我们自己的TextView的标题
     * 4. 返回的箭头的展示或隐藏
     */

    private BaseActivity mBaseActivity;
    private TextView mTvTitle;

    // 在Activity里面使用
    public ToolbarWrapper(BaseActivity baseActivity){
        mBaseActivity = baseActivity;
        Toolbar toolbar = findById(baseActivity, R.id.standard_toolbar);
        init(toolbar);

        // 标题不设置(TextView展示)、返回箭头有的
        setShowBack(true);// 显示返回箭头
        setShowTitle(false);// 不显示默认标题

    }

    // 在Fragment
    public ToolbarWrapper(BaseFragment fragment) {
        mBaseActivity = (BaseActivity) fragment.getActivity();
        Toolbar toolbar = ButterKnife.findById(fragment.getView(),R.id.standard_toolbar);
        init(toolbar);

        // Fragment显示选项菜单
        fragment.setHasOptionsMenu(true);

        // 标题不设置(TextView展示)、返回箭头没有
        setShowBack(false);
        setShowTitle(false);
    }

    // 绑定文本和设置ActionBar
    private void init(Toolbar toolbar) {
        // 找到标题的textView
        mTvTitle = ButterKnife.findById(toolbar,R.id.standard_toolbar_title);
        // 设置Toolbar作为Actionbar展示
        mBaseActivity.setSupportActionBar(toolbar);
    }

    // 为了方便链式调用，所以返回值为本身
    public ToolbarWrapper setShowBack(boolean isShowback) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isShowback);
        return this;
    }

    public ToolbarWrapper setShowTitle(boolean isShowTitle) {
        getSupportActionBar().setDisplayShowTitleEnabled(isShowTitle);
        return this;
    }

    // 设置自定义标题
    public ToolbarWrapper setCustomTitle(int resId){
        if (mTvTitle == null) {
            throw new UnsupportedOperationException("No title TextView in Toolbar");
        }
        mTvTitle.setText(resId);
        return this;
    }

    private ActionBar getSupportActionBar(){
        return mBaseActivity.getSupportActionBar();
    }
}
