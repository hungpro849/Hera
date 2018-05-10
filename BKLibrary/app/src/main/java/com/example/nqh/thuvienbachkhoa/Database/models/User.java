package com.example.nqh.thuvienbachkhoa.Database.models;

import com.j256.ormlite.field.ForeignCollectionField;

import java.util.List;

/**
 * Created by hoang on 4/24/2018.
 */

public class User extends GeneralUser {
    /*==================================
    * DESCRIPTIONS
    * ==================================
    * Fields:
    * userCart: Cart
    * locked : boolean
    * borrowedBookList : List<Book> - list of borrowed book of each user
    * wantedBookList : List<Book> - list of books in cart of the user
    * extendTimes : int - times the user want to borrowed books longer
    * */

    private Boolean locked;

    private int extendTimes;

    private List<Book> borrowedBookList;

    private List<Book> wantedBookList;



}
