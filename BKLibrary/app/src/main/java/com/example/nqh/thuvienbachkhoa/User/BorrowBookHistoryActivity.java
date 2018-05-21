package com.example.nqh.thuvienbachkhoa.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.ArrayList;
import java.util.List;

public class BorrowBookHistoryActivity extends AppCompatActivity {
    private List<BookInfoView> mDataset = new ArrayList<>();
    private List<BookInfoView> mDatasetBackup = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BooksAdapter mBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_book);


        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
    }


}
