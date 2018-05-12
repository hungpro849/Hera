package com.example.nqh.thuvienbachkhoa.Interface;

import com.example.nqh.thuvienbachkhoa.Model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CallAPI {
    // Login
    @FormUrlEncoded
    @POST("auth/local")
    Call<TokenResponse> doLogin(@Field("identifier") String identifier, @Field("password") String password);

    // Register
    @FormUrlEncoded
    @POST("auth/local/register")
    Call<TokenResponse> doRegister(@Field("email") String email, @Field("password") String password, @Field("username") String username,
                                   @Field("address") String address, @Field("fullname") String fullname, @Field("phone") String phone);

    // Forgot password
}
