package com.example.nqh.thuvienbachkhoa.Interface;

import com.example.nqh.thuvienbachkhoa.Model.Book;
import com.example.nqh.thuvienbachkhoa.Model.BorrowTransaction;
import com.example.nqh.thuvienbachkhoa.Model.TokenResponse;
import com.example.nqh.thuvienbachkhoa.Model.User;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    // Update user data
    @FormUrlEncoded
    @PUT("user/me")
    Call<User> doUpdate(@Header("Authorization") String authHeader, @Field("phone") String phone,
                        @Field("address") String address, @Field("fullname") String fullname);

    // Change password
    @FormUrlEncoded
    @PUT("user/me")
    Call<User> doUpdatePassword(@Header("Authorization") String authHeader, @Field("password") String password);

    // Forgot password
    @FormUrlEncoded
    @POST("auth/forgot-password")
    Call<JSONObject> doResetPassword(@Field("email") String email, @Field("url") String url);

    // Get Book
    @GET("book")
    Call<List<Book>> getBooks();

    // Create a new book
    @FormUrlEncoded
    @POST("book")
    Call<Book> createBook(@Header("Authorization") String authHeader,@Field("name") String name, @Field("author") String author, @Field("subject") String subject,
                                   @Field("description") String description, @Field("image_link") String image_link, @Field("stock") String stock);

    // Update book
    @FormUrlEncoded
    @PUT("book/{id}")
    Call<Book> editBook(@Header("Authorization") String authHeader, @Path("id") String id, @Field("name") String name, @Field("author") String author, @Field("subject") String subject,
                                @Field("description") String description, @Field("image_link") String image_link, @Field("stock") String stock);

    // Delete book
    @DELETE("book/{id}")
    Call<Book> delBook(@Header("Authorization") String authHeader, @Path("id") String id);

    // Get Book
    @GET("user")
    Call<List<User>> getUsers(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @PUT("user/{id}")
    Call<User> editUser(@Header("Authorization") String authHeader, @Path("id") String id, @Field("password") String password,@Field("address") String address,
                        @Field("phone") String phone, @Field("fullname") String fullname);

    @DELETE("user/{id}")
    Call<User> delUser(@Header("Authorization") String authHeader, @Path("id") String id);

    // Borrow a book
    @FormUrlEncoded
    @POST("transaction/me")
    Call<JSONObject> borrowBook(@Header("Authorization") String authHeader, @Field("book") String bookID);

    // Get borrow history
    @GET("transaction/me")
    Call<BorrowTransaction> getBorrowHistory(@Header("Authorization") String authHeader);

    @GET("book/{id}/stock")
    Call<JsonObject> getRemain(@Path("id") String id);

}
