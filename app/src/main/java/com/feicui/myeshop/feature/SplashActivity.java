package com.feicui.myeshop.feature;

import android.animation.Animator;
import android.content.Intent;
import android.widget.ImageView;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseActivity;

import butterknife.BindView;

public class SplashActivity extends BaseActivity implements Animator.AnimatorListener {

    @BindView(R.id.image_splash)
    ImageView mImageSplash;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_splash;
    }

    // 完成视图的操作
    protected void initView() {

        mImageSplash.setAlpha(0.3f);
        // 从开始通过动画透明再变化
        mImageSplash.animate()
                .alpha(1.0f) // 设置透明度动画
                .setDuration(2000) // 设置动画持续时间
                .setListener(this) // 设置动画的监听
                .start(); // 开始动画

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Intent intent = new Intent(this,EShopMainActivity.class);
        startActivity(intent);
        // 设置转场的效果
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        finish();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
