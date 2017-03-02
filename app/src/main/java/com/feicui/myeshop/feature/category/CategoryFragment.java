package com.feicui.myeshop.feature.category;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseFragment;
import com.feicui.myeshop.base.wrapper.ToolbarWrapper;
import com.feicui.myeshop.feature.search.SearchGoodsActivity;
import com.feicui.myeshop.network.ApiPath;
import com.feicui.myeshop.network.EShopClient;
import com.feicui.myeshop.network.ResponseEntity;
import com.feicui.myeshop.network.UICallback;
import com.feicui.myeshop.network.entity.CategoryPrimary;
import com.feicui.myeshop.network.entity.CategoryRsp;
import com.feicui.myeshop.network.entity.Filter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

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
            UICallback uiCallback = new UICallback() {
                @Override
                public void onBusinessResponse(boolean isSucces, ResponseEntity responseEntity) {
                    if (isSucces) {
                        CategoryRsp categoryRsp = (CategoryRsp) responseEntity;
                        mData = categoryRsp.getData();
                        // 数据有了之后，数据给一级分类，默认选择第一条，二级分类才能展示
                        updateCategory();
                    }
                }
            };
            EShopClient.getInstance().enqueue(ApiPath.CATEGORY, null, CategoryRsp.class, uiCallback);
        }
    }

    //更新分类数据
    private void updateCategory() {
        mCategoryAdapter.reset(mData);
        // 切换展示二级分类
        chooseCategory(0);
    }

    //用于根据一级分类的选项展示二级分类的内容
    private void chooseCategory(int position) {
        mListCategory.setItemChecked(position, true);
        mChildrenAdapter.addAll(mCategoryAdapter.getItem(position).getChildren());
    }

    // 点击一级分类：展示相应二级分类
    @OnItemClick(R.id.list_category)
    public void onItemClick(int position) {
        chooseCategory(position);
    }

    // 点击二级分类
    @OnItemClick(R.id.list_children)
    public void onChildrenClick(int position) {
        //跳转到搜索页面
        int categoryId = mChildrenAdapter.getItem(position).getId();
        navigateToSearch(categoryId);
    }

    private void navigateToSearch(int categoryId) {
        // 根据id构建Filter，然后跳转页面
        Filter filter = new Filter();
        filter.setCategoryId(categoryId);
        Intent intent = SearchGoodsActivity.getStartIntent(getContext(), filter);
        getActivity().startActivity(intent);
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
            //跳转到搜索页面
            int position = mListCategory.getCheckedItemPosition();
            int id = mCategoryAdapter.getItem(position).getId();
            navigateToSearch(id);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
