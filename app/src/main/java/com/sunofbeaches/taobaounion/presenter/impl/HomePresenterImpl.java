package com.sunofbeaches.taobaounion.presenter.impl;

import com.sunofbeaches.taobaounion.model.Api;
import com.sunofbeaches.taobaounion.model.domain.Categories;
import com.sunofbeaches.taobaounion.presenter.IHomePresenter;
import com.sunofbeaches.taobaounion.utils.LogUtils;
import com.sunofbeaches.taobaounion.utils.RetrofitManager;
import com.sunofbeaches.taobaounion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {

    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        //加载分类数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call,Response<Categories> response) {
                //数据结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this,"result code is -- > " + code);
                if(code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    Categories categories = response.body();
                    //LogUtils.d(HomePresenterImpl.this,categories.toString());
                    if(mCallback != null) {
                        mCallback.onCategoriesLoaded(categories);
                    }
                } else {
                    //请求失败
                    LogUtils.i(HomePresenterImpl.this,"请求失败...");
                }
            }

            @Override
            public void onFailure(Call<Categories> call,Throwable t) {
                //加载失败的结果
                LogUtils.e(HomePresenterImpl.this,"请求错误..." + t);
            }
        });
    }

    @Override
    public void registerCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterCallback(IHomeCallback callback) {
        mCallback = null;
    }
}
