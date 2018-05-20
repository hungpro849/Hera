package com.example.nqh.thuvienbachkhoa.User;

public class BookInfoView {

    String name;
    String image;
    String id;
    String author;
    String year;

    public BookInfoView(String name, String image, String id, String author, String year) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.author = author;
        this.year = year;
    }



    public BookInfoView(String name, String image, String id) {
        this.name = name;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
