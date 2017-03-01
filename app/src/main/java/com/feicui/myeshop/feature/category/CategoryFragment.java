package com.feicui.myeshop.feature.category;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseFragment;
import com.feicui.myeshop.base.wrapper.ToastWrapper;
import com.feicui.myeshop.base.wrapper.ToolbarWrapper;
import com.feicui.myeshop.network.EShopClient;
import com.feicui.myeshop.network.UICallback;
import com.feicui.myeshop.network.entity.CategoryPrimary;
import com.feicui.myeshop.network.entity.CategoryRsp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/2/24.
 */

public class CategoryFragment extends BaseFragment {

    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_category)
    ListView mListCategory;
    @BindView(R.id.list_children)
    ListView mListChildren;
    private AppCompatActivity mActivity;
    private List<CategoryPrimary> mData;
    private CategoryAdapter mCategoryAdapter;
    private ChildrenAdapter mChildrenAdapter;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_category;
    }

    protected void initView() {

        initToolbar();

        // ListView的展示
        mCategoryAdapter = new CategoryAdapter();
        mListCategory.setAdapter(mCategoryAdapter);

        mChildrenAdapter = new ChildrenAdapter();
        mListChildren.setAdapter(mChildrenAdapter);

        if (mData != null) {
            // 可以直接更新UI
            updateCategory();
        } else {
            // 去进行网络请求拿到数据
            Call call = EShopClient.getInstance().getCategory();
            call.enqueue(new UICallback() {
                @Override
                public void onFailureInUI(Call call, IOException e) {
                    ToastWrapper.show("请求失败"+e.getMessage());
                }

                @Override
                public void onResponseInUI(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        CategoryRsp categoryRsp = new Gson().fromJson(body.string(), CategoryRsp.class);
                        if (categoryRsp.getStatus().isSucceed()) {
                            mData = categoryRsp.getData();
                            // 数据有了之后，数据给一级分类，默认选择第一条，二级分类才能展示
                            updateCategory();
                        }
                    }
                }
            });
        }
    }

    //更新分类数据
    private void updateCategory(){
        mCategoryAdapter.reset(mData);
        // 切换展示二级分类
        chooseCategory(0);
    }

    //用于根据一级分类的选项展示二级分类的内容
    private void chooseCategory(int position) {
        mListCategory.setItemChecked(position,true);
        mChildrenAdapter.addAll(mCategoryAdapter.getItem(position).getChildren());
    }

    // 点击一级分类：展示相应二级分类
    @OnItemClick(R.id.list_category)
    public void onItemClick(int position){
        chooseCategory(position);
    }

    // 点击二级分类
    @OnItemClick(R.id.list_children)
    public void onChildrenClick(int position){
        // TODO: 2017/2/24 会完善到跳转页面的
        String name = mChildrenAdapter.getItem(position).getName();
        ToastWrapper.show(name);
    }

    private void initToolbar() {
        // 利用包装的toolbar
        new ToolbarWrapper(this).setCustomTitle(R.string.category_title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) mActivity.onBackPressed();
        if (item.getItemId() == R.id.menu_search) {
            // TODO: 2017/2/24 跳转到搜索页面
            ToastWrapper.show("搜索");
        }
        return super.onOptionsItemSelected(item);
    }
}
