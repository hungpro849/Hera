package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.UserBook;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class BorrowerInfoFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DBHelper database;
    private Toolbar mToolbar;
    private TextView mUserNameTextView;
    private TextView mUserEmailTextView;
    private TextView mBorrowDateTextView;
    private GeneralUser currentBorrower;
    private List<BookInfoInList> mDataset = new Vector<BookInfoInList>();

    public void setDatabase(DBHelper db) {
        this.database = db;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setCurrentBorrower(GeneralUser user) {
        this.currentBorrower = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrower_info, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.borrowed_books_recycle_view);
        mToolbar = (Toolbar) view.findViewById(R.id.borrower_info_tool_bar);
        mUserNameTextView = (TextView) view.findViewById(R.id.borrower_name);
        mUserEmailTextView = (TextView) view.findViewById(R.id.borrower_email);
        mBorrowDateTextView = (TextView) view.findViewById(R.id.borrower_date);
        mDataset.clear();
        setUpRecyclerView();
        setupToolbar();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AdminActivity.mActionBar.hide();
    }

    public void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);

        loadBooks();

        mAdapter = new BookListAdapter(mDataset, database);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
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

    public void loadBooks() {
        try {
            List<UserBook> allBorrowedBooks = database.queryEqual(UserBook.class, "user_id", currentBorrower);
            for (UserBook b: allBorrowedBooks) {
                mDataset.add(new BookInfoInList(b.getBook()));
            }
        } catch (Exception e) {
            Log.e("UserBook Exception",e.getMessage());
        }

    }

}
