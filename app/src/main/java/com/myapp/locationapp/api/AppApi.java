package com.myapp.locationapp.api;

import com.myapp.locationapp.model.BaseResponse;
import com.myapp.locationapp.model.DlUl;
import com.myapp.locationapp.model.News;
import com.myapp.locationapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ishan on 27-11-2017.
 */

public interface AppApi {

    @POST("UserLogin.php")
    Call<BaseResponse<User>> getLogin(@Body User user);

    @POST("UserRegister.php")
    Call<BaseResponse<User>> getRegister(@Body User user);

    @GET("GetNewsHeadlines.php")
    Call<BaseResponse<News>> getNewsHeadlines();

    @POST("AddNewsHeadlines.php")
    Call<BaseResponse<News>> addNewsHeadlines(@Body News news);

    @POST("AddDlUl.php")
    Call<BaseResponse<DlUl>> addDlUl(@Body DlUl dlUl);
}
