package com.example.nqh.thuvienbachkhoa.Interface;

import com.example.nqh.thuvienbachkhoa.Model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginAPI {
    @FormUrlEncoded
    @POST("auth/local")
    Call<TokenResponse> doLogin(@Field("identifier") String identifier, @Field("password") String password);
}
