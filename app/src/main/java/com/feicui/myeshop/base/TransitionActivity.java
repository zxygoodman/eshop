package com.feicui.myeshop.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.feicui.myeshop.R;

/**
 * 有转场动画的基类
 */

public class TransitionActivity extends AppCompatActivity{

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // 设置转场动画
        setTransitionAnimation(true);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        setTransitionAnimation(true);
    }

    @Override
    public void finish() {
        super.finish();
        setTransitionAnimation(false);
    }

    public void finishWithDefault(){
        super.finish();
    }

    private void setTransitionAnimation(boolean isNewActivity){
        if (isNewActivity) {
            // 新页面从右边进入
            overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
        }else {
            // 回到上个页面
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
    }
}
