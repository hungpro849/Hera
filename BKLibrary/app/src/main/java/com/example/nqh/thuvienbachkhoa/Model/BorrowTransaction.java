package com.example.nqh.thuvienbachkhoa.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BorrowTransaction {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("book")
    @Expose
    private Book book;
    @SerializedName("user")
    @Expose
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}