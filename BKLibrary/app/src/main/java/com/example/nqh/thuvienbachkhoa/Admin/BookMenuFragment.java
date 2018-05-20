package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.List;
import java.util.Vector;

public class BookMenuFragment extends Fragment {
    private RecyclerView mMostPopularRecyclerView;
    private RecyclerView mMostBorrowedRecyclerView;
    private RecyclerView mNewBookRecyclerView;
    private RecyclerView.Adapter mMostPopularAdapter;
    private RecyclerView.Adapter mMostBorrowedAdapter;
    private RecyclerView.Adapter mNewBookAdapter;
    private RecyclerView.LayoutManager mMostPopularLayoutManager;
    private RecyclerView.LayoutManager mMostBorrowedLayoutManager;
    private RecyclerView.LayoutManager mNewBookLayoutManager;
    private Activity mCurrentActivity;

    public void setCurrentActivity(Activity activity) {
        this.mCurrentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_menu, container, false);
        mMostPopularRecyclerView = view.findViewById(R.id.most_popular_book_recycler_view);
        mMostBorrowedRecyclerView = view.findViewById((R.id.most_borrowed_book_recycler_view));
        mNewBookRecyclerView = view.findViewById(R.id.new_book_recycler_view);
        setUpRecyclerView();
        return view;
    }

    public void setUpRecyclerView() {
        mMostPopularRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        List<BookInfoInMenu> myDataset = new Vector<BookInfoInMenu>();

        for (int i = 1; i <= 8; i++) {
            BookInfoInMenu newInfo = new BookInfoInMenu("Title" + i, R.drawable.bookex);
            myDataset.add(newInfo);
        }
        mMostPopularAdapter = new BookMenuAdapter(myDataset);
        mMostBorrowedAdapter = new BookMenuAdapter(myDataset);
        mNewBookAdapter = new BookMenuAdapter(myDataset);

        mMostPopularRecyclerView.setAdapter(mMostPopularAdapter);
        mMostBorrowedRecyclerView.setAdapter(mMostBorrowedAdapter);
        mNewBookRecyclerView.setAdapter(mNewBookAdapter);

        mMostPopularLayoutManager = new LinearLayoutManager(mCurrentActivity, LinearLayoutManager.HORIZONTAL,false);
        mMostBorrowedLayoutManager = new LinearLayoutManager(mCurrentActivity, LinearLayoutManager.HORIZONTAL,false);
        mNewBookLayoutManager = new LinearLayoutManager(mCurrentActivity, LinearLayoutManager.HORIZONTAL,false);

        mMostPopularRecyclerView.setLayoutManager(mMostPopularLayoutManager);
        mMostBorrowedRecyclerView.setLayoutManager(mMostBorrowedLayoutManager);
        mNewBookRecyclerView.setLayoutManager(mNewBookLayoutManager);
    }
}
