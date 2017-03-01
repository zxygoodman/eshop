package com.feicui.myeshop.base.widgets.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.feicui.myeshop.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 自定义的轮播图控件
 * 1. 自动轮播
 * 2. 数据可随意设置(适配器的问题)
 * 3. 自动和手动的冲突：触屏的时间+轮播时间
 */
public class BannerLayout extends RelativeLayout {

    @BindView(R.id.pager_banner)
    ViewPager mPagerBanner;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;
    private Timer mCycleTimer;
    private TimerTask mCycleTask;
    private CyclingHandler mHandler;
    private static final long duration = 4000;
    private long mResumecycleTime;

    // 代码中使用控件
    public BannerLayout(Context context) {
        super(context);
        init(context);
    }

    // 布局中使用，但是未设置style
    public BannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // 设置了style
    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Merge标签一定要设置ViewGroup和attachToRoot为true
        LayoutInflater.from(context).inflate(R.layout.widget_banner_layout, this, true);
        ButterKnife.bind(this);
        mHandler = new CyclingHandler(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 在视图上显示出来的时候

        // 计时器
        mCycleTimer = new Timer();
        // 定时的发送一些事件：使用Handler来发送，并且处理
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                // 定时的发送一些事件：使用Handler来发送，并且处理
                mHandler.sendEmptyMessage(0);
            }
        };
        // 任务（事件）、延时事件、循环时间
        mCycleTimer.schedule(mCycleTask, duration, duration);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 取消我们开启的计时任务
        mCycleTimer.cancel();
        mCycleTask.cancel();
        mCycleTimer = null;
        mCycleTask = null;
    }

    // 首先获取到我们触摸的时间
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mResumecycleTime = System.currentTimeMillis() + duration;
        return super.dispatchTouchEvent(ev);

    }

    // 切换到下一页的方法
    public void moveToNextPosition() {
        // 看有没有设置适配器
        if (mPagerBanner.getAdapter()==null){
            throw new IllegalStateException("you need set a banner adapter");
        }
        // 看适配器里面是不是有数据
        int count = mPagerBanner.getAdapter().getCount();
        if (count==0) return;
        // 看是不是展示的最后一条
        if (mPagerBanner.getCurrentItem()==count-1){
            // 切换到0,不设置平滑滚动
            mPagerBanner.setCurrentItem(0,false);
        }else {
            mPagerBanner.setCurrentItem(mPagerBanner.getCurrentItem()+1,true);
        }

    }

    // 设置适配器的方法
    public void setAdapter(BannerAdapter adapter){
        mPagerBanner.setAdapter(adapter);
        mIndicator.setViewPager(mPagerBanner);
        adapter.registerDataSetObserver(mIndicator.getDataSetObserver());
    }

    // 为了防止内部类持有外部类的引用而造成内存泄漏，所以静态内部类+弱引用的方式
    private static class CyclingHandler extends Handler {

        private WeakReference<BannerLayout> mBannerReference;

        public CyclingHandler(BannerLayout banner) {
            mBannerReference = new WeakReference<BannerLayout>(banner);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 接收到消息，处理：轮播图切换到下一页
            if (mBannerReference == null) return;
            BannerLayout bannerLayout = mBannerReference.get();
            if (bannerLayout == null) return;

            // 触摸之后时间还没过四秒，不去轮播
            if (System.currentTimeMillis() < bannerLayout.mResumecycleTime) return;
            // 切换到下一页
            bannerLayout.moveToNextPosition();
        }
    }
}
