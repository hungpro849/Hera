package com.example.nqh.thuvienbachkhoa.User;

public class line_main_user_infor {
    int id;
    String name;
    int icon;

    String author;
    String subject;
    String description;
    float score;
    String image_link;
    int voters;
    int remain;

    public line_main_user_infor(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }
    public line_main_user_infor(int id, String name, String author, String subject, String description, float score, String image_link, int voters, int remain) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.subject = subject;
        this.description = description;
        this.score = score;
        this.image_link = image_link;
        this.voters = voters;
        this.remain = remain;
    }

    public String getName() {
        return this.name;
    }

    public int getIcon() {
        return this.icon;
    }
}
