package com.example.nqh.thuvienbachkhoa.Database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by hoang on 5/5/2018.
 */

@DatabaseTable(tableName = "user_book")
public class UserBook {
    /*==================================
    * DESCRIPTIONS
    * ==================================
    * Fields:
    * id : int
    * user: GeneralUser
    * book_id: Book
    * borrowed_date: long
    * pay_date: long
    * This class create many2many relation between user and book
    * A book is borrowed by many users and a user can borrow many books
    * */

    /*==================================
    * FIELDS
    * ==================================
    * */
    @DatabaseField(columnName = "id", generatedId = true)
    int id;

    @DatabaseField(columnName = "user_id", foreign = true, index = true ,foreignAutoCreate = true, foreignAutoRefresh = true)
    GeneralUser user;


    @DatabaseField(columnName = "book_id", foreign = true, index = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    Book book;

    @DatabaseField(columnName = "borrowed_date")
    long borrowed_date;

    @DatabaseField(columnName = "pay_date")
    long pay_date;

    public UserBook() {}

    public UserBook(GeneralUser user, Book book, Date borrowed_date) {
        this.user = user;
        this.book = book;
        this.borrowed_date = borrowed_date.getTime();
    }

    public UserBook(GeneralUser user, Book book) {
        this.user = user;
        this.book = book;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser(GeneralUser user) {
        this.user = user;
    }

    public GeneralUser getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getPay_date() {
        return new Date(this.pay_date);
    }

    public void setPay_date(Date pay_date) {
        this.pay_date = pay_date.getTime();
    }

    public Date getBorrowed_date() {
        return new Date(this.borrowed_date);
    }

    public void setBorrowed_date(Date borrowed_date) {
        this.borrowed_date = borrowed_date.getTime();
    }
}
