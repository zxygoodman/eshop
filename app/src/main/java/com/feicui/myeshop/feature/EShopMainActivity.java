package com.feicui.myeshop.feature;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseActivity;
import com.feicui.myeshop.base.TestFragment;
import com.feicui.myeshop.feature.category.CategoryFragment;
import com.feicui.myeshop.feature.home.HomeFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;

public class EShopMainActivity extends BaseActivity implements OnTabSelectListener {

    @BindView(R.id.bottom_bar)
    BottomBar mBottomBar;

    private HomeFragment mHomeFragment;
    private CategoryFragment mCategoryFragment;
    private TestFragment mCartFragment;
    private TestFragment mMineFragment;

    private Fragment mCurrentFragment;

    /**
     * 非静态的内部类(匿名内部类)，会持有外部类的引用，会造成内存泄漏
     * 处理方法：使用静态的内部类，并将外部类引用存到弱引用当中
     * private WeakReference<Activity> mWeakReference
     */

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_eshop_main;
    }

    // 视图的初始化操作
    protected void initView() {

        // 看一下Fragmentmanager里面是不是已经有了这些Fragment
        retrieveFragment();

        // 设置导航选择的监听事件
        mBottomBar.setOnTabSelectListener(this);
    }

    // 底部导航栏某一项选择的时候触发
    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                }
                // 切换Fragment
                switchfragment(mHomeFragment);
                break;
            case R.id.tab_category:
                if (mCategoryFragment == null) {
                    mCategoryFragment = CategoryFragment.newInstance();
                }
                switchfragment(mCategoryFragment);
                break;
            case R.id.tab_cart:
                if (mCartFragment == null) {
                    mCartFragment = TestFragment.newInstance("CartFragment");
                }
                switchfragment(mCartFragment);
                break;
            case R.id.tab_mine:
                if (mMineFragment == null) {
                    mMineFragment = TestFragment.newInstance("MineFragment");
                }
                switchfragment(mMineFragment);
                break;
            default:
                throw new UnsupportedOperationException("unsupport");
        }
    }

    //切换Fragment
    private void switchfragment(Fragment target) {

        // show hide的方式
        if (mCurrentFragment == target) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        if (target.isAdded()) {
            transaction.show(target);
        } else {
            String text;
            if (target instanceof TestFragment) {
                text = ((TestFragment) target).getArgumentText();
            }else {
                text = target.getClass().getName();
            }
            transaction.add(R.id.layout_container, target, text);
        }
        transaction.commit();
        mCurrentFragment = target;

    }

    private void retrieveFragment() {
        FragmentManager manager = getSupportFragmentManager();
        mHomeFragment = (HomeFragment) manager.findFragmentByTag("HomeFragment");
        mCategoryFragment = (CategoryFragment) manager.findFragmentByTag("CategoryFragment");
        mCartFragment = (TestFragment) manager.findFragmentByTag("CartFragment");
        mMineFragment = (TestFragment) manager.findFragmentByTag("MineFragment");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCurrentFragment != mHomeFragment) {
            // 如果不是在首页，就切换首页上
            mBottomBar.selectTabWithId(R.id.tab_home);
            return;
        }
        // 是首页，我们不去关闭，退到后台运行
        moveTaskToBack(true);
    }
}
