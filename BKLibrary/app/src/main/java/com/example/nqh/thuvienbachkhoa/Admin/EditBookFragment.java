package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditBookFragment extends Fragment {
    public Toolbar mToolbar;
    DBHelper database;
    EditText mBookName;
    EditText mBookAuthor;
    EditText mBookSubject;
    EditText mBookDescription;
    EditText mBookRemain;
    String mName;
    String mAuthor;
    String mSubject;
    String mDescription;
    int mRemain;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_book, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.edit_book_tool_bar);

        mBookName = (EditText) view.findViewById(R.id.edit_book_name);
        mBookAuthor = (EditText) view.findViewById(R.id.edit_book_author);
        mBookSubject = (EditText) view.findViewById(R.id.edit_book_subject);
        mBookDescription = (EditText) view.findViewById(R.id.edit_book_description);
        mBookRemain = (EditText) view.findViewById(R.id.edit_book_remain);

        mBookName.setText(mName);
        mBookAuthor.setText(mAuthor);
        mBookSubject.setText(mSubject);
        mBookDescription.setText(mDescription);
        mBookRemain.setText(Integer.toString(mRemain));
        mToolbar.setTitle(null);


        setupToolbar();
        setupEditButton();
        return view;
    }

    public void setDatabase(DBHelper db) {
        this.database = db;
    }

    public void displayInitialData(String name, String author, String subject, String description, int remain) {
        mName = name;
        mAuthor = author;
        mSubject = subject;
        mDescription = description;
        mRemain = remain;
    }

    public void setupToolbar() {
        mToolbar.setNavigationIcon(R.drawable.back_button_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
    public void setupEditButton() {
        mToolbar.inflateMenu(R.menu.add_book_menu_item);
        mToolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String bName = mBookName.getText().toString().trim();
                String bAuthor = mBookAuthor.getText().toString().trim();
                String bSubject = mBookSubject.getText().toString().trim();
                String bDescription = mBookDescription.getText().toString().trim();
                String bRemain = mBookRemain.getText().toString().trim();

                if (!checkEditBookCondition(bName,bAuthor,bSubject,bDescription,bRemain)) {
                    try {
                        Book currentBook = getCurrentBook();
                        currentBook.setName(bName);
                        currentBook.setAuthor(bAuthor);
                        currentBook.setSubject(bSubject);
                        currentBook.setDescription(bDescription);
                        currentBook.setRemain(Integer.parseInt(bRemain));
                        database.createOrUpdate(currentBook);
                        Toast.makeText(getActivity(), "Chỉnh sửa sách thành công", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Chỉnh sửa sách thất bại", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Xin hãy nhập đầy đủ vào các trường", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public boolean checkEditBookCondition(String bName, String bAuthor, String bSubject, String bDescription, String bRemain) {
        //return true if strings are empty or null
        boolean conditionName = TextUtils.isEmpty(bName);
        boolean conditionAuthor = TextUtils.isEmpty(bAuthor);
        boolean conditionSubject = TextUtils.isEmpty(bSubject);
        boolean conditionDescription = TextUtils.isEmpty(bDescription);
        boolean conditionRemain = TextUtils.isEmpty(bRemain);
        return conditionName || conditionAuthor || conditionSubject || conditionDescription || conditionRemain;
    }

    public Book getCurrentBook() {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("name", mName);
        List<Book> currentBooks = Collections.emptyList();
        try {
            currentBooks = database.query(Book.class, condition);
            if (currentBooks.size() > 0) {
                return currentBooks.get(0);
            }
        } catch (Exception e) {
            Log.e("Get current book failed", e.getMessage());
        }
        return null;
    }
}
