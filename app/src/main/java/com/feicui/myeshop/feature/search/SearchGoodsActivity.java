package com.feicui.myeshop.feature.search;

import android.content.Context;
import android.content.Intent;

import com.feicui.myeshop.R;
import com.feicui.myeshop.base.BaseActivity;
import com.feicui.myeshop.network.entity.Filter;
import com.google.gson.Gson;

public class SearchGoodsActivity extends BaseActivity {

    private static final String EXTRA_SEARCH_FILTER = "EXTRA_SEARCH_FILTER";

    public static Intent getStartIntent(Context context, Filter filter){
        Intent intent = new Intent(context,SearchGoodsActivity.class);
        intent.putExtra(EXTRA_SEARCH_FILTER,new Gson().toJson(filter));
        return intent;
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_search_goods;
    }

    @Override
    protected void initView() {

    }

}
