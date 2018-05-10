package com.example.nqh.thuvienbachkhoa.User;

public class msBookInfor {
    String name;
    int icon;
    int idOfUserBook;

    public int getIcon() {
        return icon;
    }

    public msBookInfor(String name, int icon,int id) {
        this.name = name;
        this.icon=icon;
        this.idOfUserBook=id;

    }

    public String getName() {
        return this.name;
    }

    public int getIdOfUserBook() {
        return this.idOfUserBook;
    }
}
