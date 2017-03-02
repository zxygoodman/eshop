package com.feicui.myeshop.network;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/2/24.
 */

public class EShopClient {

    public static final String BASE_URL = "http://106.14.32.204/eshop/emobile/?url=";

    private static EShopClient sEShopClient;
    private final OkHttpClient mOkHttpClient;
    private final Gson mGson;

    public static synchronized EShopClient getInstance() {
        if (sEShopClient == null) {
            sEShopClient = new EShopClient();
        }
        return sEShopClient;
    }

    private EShopClient() {

        mGson = new Gson();

        // 日志拦截器的创建
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient的初始化
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

//    // 分类页面：商品分类请求
//    public Call getCategory() {
//        Request request = new Request.Builder()
//                .get()
//                .url(BASE_URL + "/category")
//                .build();
//
//        return mOkHttpClient.newCall(request);
//    }
//
//    // 首页：banner请求接口
//    public Call getHomeBanner(){
//        Request request = new Request.Builder()
//                .get()
//                .url(BASE_URL+"/home/data")
//                .build();
//        return mOkHttpClient.newCall(request);
//    }
//
//    // 首页：分类和推荐的商品
//    public Call getHomeCategory(){
//        Request request = new Request.Builder()
//                .get()
//                .url(BASE_URL+"/home/category")
//                .build();
//        return mOkHttpClient.newCall(request);
//    }
//
//    // 搜索：搜索商品
//    public Call getSearch(SearchReq searchReq){
//        String param = new Gson().toJson(searchReq);
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("json",param)
//                .build();
//
//        Request request = new Request.Builder()
//                .post(requestBody)
//                .url(BASE_URL+"/search")
//                .build();
//        return mOkHttpClient.newCall(request);
//    }

    // 在单元测试的时候是同步请求直接拿到结果的，代码是做异步回调的方式。
    // 为了方便，我们把同步和异步都提供出来。

    // 同步：直接拿到response里面的实体类数据
    public <T extends ResponseEntity>T execute(String path,
                                               RequestParam requestParam,
                                               Class<T> clazz) throws IOException {

        // 把请求的构建写到一个方法里面
        Response response = newApiCall(path, requestParam).execute();

        // 异步里面会不会也用到呢？所以写到一个方法里去
        return getResponseEntity(response,clazz);
    }

    // 异步回调：最后要创建UICallBack
    public Call enqueue(String path,
                        RequestParam requestParam,
                        Class<? extends ResponseEntity> clazz,
                        UICallback uiCallback){

        // 构建call模型
        Call call = newApiCall(path, requestParam);
        uiCallback.setResponseType(clazz);
        // 为了规范，我们在方法里面直接执行异步方法，就需要一个UiCallback，所以通过参数传递
        call.enqueue(uiCallback);
        return call;
    }

    private Call newApiCall(String path, RequestParam requestParam) {

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);

        // 有请求体的话，是Post请求
        if (requestParam!=null){
            String json = mGson.toJson(requestParam);
            RequestBody requestBody = new FormBody.Builder()
                    .add("json",json)
                    .build();
            builder.post(requestBody);
        }
        Request request = builder.build();

        return mOkHttpClient.newCall(request);
    }

    // 根据响应Response，将响应体转换成响应的实体类
    public <T extends ResponseEntity>T getResponseEntity(Response response,Class<T> clazz) throws IOException {
        // 没有成功
        if (!response.isSuccessful()){
            throw new IOException("Response code is"+response.code());
        }
        // 成功，转换成相应的实体类了
        return mGson.fromJson(response.body().string(),clazz);
    }
}
