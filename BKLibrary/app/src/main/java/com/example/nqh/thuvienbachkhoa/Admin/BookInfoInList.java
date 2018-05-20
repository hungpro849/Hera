package com.example.nqh.thuvienbachkhoa.Admin;

import com.example.nqh.thuvienbachkhoa.Model.Book;

public class BookInfoInList {
    String mBookId;
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
