package com.example.nqh.thuvienbachkhoa.User;

public class BookInfoView {

    String name;
    String image;
    String id;
    String author;
    String subject;

    public BookInfoView(String name, String image, String id, String author, String subject) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.author = author;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
