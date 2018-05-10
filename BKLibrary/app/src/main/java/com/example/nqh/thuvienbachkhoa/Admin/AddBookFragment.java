package com.example.nqh.thuvienbachkhoa.Admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.R;

public class AddBookFragment extends Fragment {
    Toolbar mToolbar;
    DBHelper database;
    EditText mBookName;
    EditText mAuthor;
    EditText mSubject;
    EditText mDescription;
    EditText mRemain;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.add_book_tool_bar);

        mBookName = (EditText) view.findViewById(R.id.add_book_name);
        mAuthor = (EditText) view.findViewById(R.id.add_book_author);
        mSubject = (EditText) view.findViewById(R.id.add_book_subject);
        mDescription = (EditText) view.findViewById(R.id.add_book_description);
        mRemain = (EditText) view.findViewById(R.id.add_book_remain);

        mToolbar.setTitle(null);
        setupToolbar();
        setupAcceptButton();
        return view;
    }

    public void setDatabase(DBHelper db) {
        this.database = db;
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
    public void setupAcceptButton() {
        mToolbar.inflateMenu(R.menu.add_book_menu_item);
        mToolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String bName = mBookName.getText().toString().trim();
                String bAuthor = mAuthor.getText().toString().trim();
                String bSubject = mSubject.getText().toString().trim();
                String bDescription = mDescription.getText().toString().trim();
                String bRemain = mRemain.getText().toString().trim();

                if (!checkAddBookCondition(bName,bAuthor,bSubject,bDescription,bRemain)) {
                    try {
                        Book newBook = new Book(bName, bAuthor, bSubject, bDescription, 0.0f, null, 0, Integer.parseInt(bRemain));
                        database.fillObject(Book.class, newBook);
                        Toast.makeText(getActivity(), "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Thêm sách thất bại", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Xin hãy nhập đầy đủ vào các trường", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public boolean checkAddBookCondition(String bName, String bAuthor, String bSubject, String bDescription, String bRemain) {
        //return true if strings are empty or null
        boolean conditionName = TextUtils.isEmpty(bName);
        boolean conditionAuthor = TextUtils.isEmpty(bAuthor);
        boolean conditionSubject = TextUtils.isEmpty(bSubject);
        boolean conditionDescription = TextUtils.isEmpty(bDescription);
        boolean conditionRemain = TextUtils.isEmpty(bRemain);
        return conditionName || conditionAuthor || conditionSubject || conditionDescription || conditionRemain;
    }

}

