package com.example.nqh.thuvienbachkhoa.User;

public class BookInfoView {

    String name;
    String image;
    String id;
    String author;
    String subject;
    String description;
    String ebook_url;
    Integer stock;



    public BookInfoView(String name, String image, String id, String author, String subject, String description, String ebook_url, Integer stock) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.author = author;
        this.subject = subject;
        this.description = description;
        this.ebook_url = ebook_url;
        this.stock = stock;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEbook_url() {
        return ebook_url;
    }

    public void setEbook_url(String ebook_url) {
        this.ebook_url = ebook_url;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
