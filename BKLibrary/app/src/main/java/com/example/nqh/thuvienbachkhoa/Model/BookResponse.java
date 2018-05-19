package com.example.nqh.thuvienbachkhoa.Model;

import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookResponse {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("image_link")
    @Expose
    private String image_link;

    @SerializedName("stock")
    @Expose
    private String stock;

    private Book book;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Book getBook()
    {
        book=new Book(id,name,author,subject,description,0.0f,image_link,0,Integer.parseInt(stock));
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
