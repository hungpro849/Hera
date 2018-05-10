package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class BookListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity mCurrentActivity;
    private BookListAdapter mBookListAdapter;
    private DBHelper database;
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private FloatingActionButton mAddBookBtn;
    public List<BookInfoInList> mDataset = new Vector<BookInfoInList>();
    public List<Book> mBookList = new Vector<Book>();


    public void setCurrentActivity(Activity activity) {
        this.mCurrentActivity = activity;
    }


    public void setDatabase(DBHelper db) {
        this.database = db;
    }

    public void setDataset(List<Book> bookList) {
        mBookList = bookList;
        mDataset.removeAll(mDataset);
        for (Book b : mBookList) {
            BookInfoInList newBook = new BookInfoInList(b);
            mDataset.add(newBook);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.book_list_recycle_view);
        mToolbar = (Toolbar) view.findViewById(R.id.book_list_tool_bar);
        mAddBookBtn = (FloatingActionButton) view.findViewById(R.id.add_book_btn);
        mDataset.clear();
        mBookList.clear();

        setUpRecyclerView();
        setupToolbar();
        setupSearchView();
        setupAddBookEditButton();;
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AdminActivity.mActionBar.hide();
    }

    public void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);

        loadData();

        mAdapter = new BookListAdapter(mDataset, database);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(mCurrentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void loadData() {
        try {
            mBookList = database.getAllOrdered(Book.class, "name", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Book b : mBookList) {
            BookInfoInList newBook = new BookInfoInList(b);
            mDataset.add(newBook);
        }
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

    public void setupSearchView() {
        mToolbar.inflateMenu(R.menu.material_search_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        mSearchView = (android.support.v7.widget.SearchView) mToolbar.getMenu().getItem(0).getActionView();
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadDataAfterQuery(bookQuery(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mToolbar.getMenu().getItem(0).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mToolbar.setBackgroundColor(Color.WHITE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
                return true;
            }
        });
    }

    public List<Book> bookQuery(String query) {
        List<Book> foundBook = Collections.emptyList();
        try {
            foundBook = database.queryLike(Book.class, "name", query);
        } catch (Exception e) {
            Log.e("Book Query Exception", e.getMessage());
        }
        return foundBook;
    }

    public void loadDataAfterQuery(List<Book> bookList) {
        mDataset.clear();
        for (Book b: bookList) {
            BookInfoInList newBook = new BookInfoInList(b);
            mDataset.add(newBook);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setupAddBookEditButton() {
        mAddBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_view, AdminActivity.mAddBookFragment)
                        .addToBackStack("Add book fragment").commit();
            }
        });
    }
}
