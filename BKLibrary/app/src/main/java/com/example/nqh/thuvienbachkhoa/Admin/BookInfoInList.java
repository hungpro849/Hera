package com.example.nqh.thuvienbachkhoa.Admin;

import android.widget.ImageView;

import com.example.nqh.thuvienbachkhoa.Database.models.Book;

public class BookInfoInList {
    int mBookId;
    String mTitle;
    String mAuthor;

    BookInfoInList(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    BookInfoInList(Book book) {
        this.mBookId = book.getId();
        this.mTitle = book.getName();
        this.mAuthor = book.getAuthor();
    }
}
