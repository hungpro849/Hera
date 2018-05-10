package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.Activity;
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

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.User;
import com.example.nqh.thuvienbachkhoa.Database.models.UserBook;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class BorrowerListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DBHelper database;
    private Toolbar mToolbar;
    private android.support.v7.widget.SearchView mSearchView;
    private List<UserInfoInList> mDataset = new Vector<UserInfoInList>();
    private List<UserBook> mBorrowerList = new Vector<UserBook>();


    public void setDatabase(DBHelper db) {
        this.database = db;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrower_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.borrower_list_recycle_view);
        mToolbar = (Toolbar) view.findViewById(R.id.borrower_list_tool_bar);
        mDataset.clear();
        setUpRecyclerView();
        setupToolbar();
        setupSearchView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AdminActivity.mActionBar.hide();
    }

    public void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);

        loadAllBorrowers();

        mAdapter = new BorrowerListAdapter(mDataset,database);
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
                loadUserAfterQuery(userQuery(query));
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

    public void getAllCurrentBorrowers() {
        // pay date = 0 means that user has not returned the books
        try {
            mBorrowerList = database.queryZeroDistinct(UserBook.class, "pay_date","user_id");
        } catch (Exception e) {
            Log.e("UserBook exception", e.getMessage());
        }
    }

    public void loadAllBorrowers() {
        getAllCurrentBorrowers();
        for (UserBook borrower: mBorrowerList) {
            UserInfoInList newBorrower = new UserInfoInList(borrower.getUser());
            mDataset.add(newBorrower);
        }
    }

    public List<GeneralUser> userQuery(String query) {
        List<GeneralUser> foundUser = Collections.emptyList();
        try {
            foundUser = database.queryLikeDistinct(GeneralUser.class, "name", query);
        } catch (Exception e) {
            Log.e("User Query Exception", e.getMessage());
        }
        return foundUser;
    }

    public void loadUserAfterQuery(List<GeneralUser> userList) {
        mDataset.clear();
        for (GeneralUser currentUser: userList) {
            try {
                List<UserBook> currentBorrowers = database.queryEqualDistinct(UserBook.class,
                        "user_id","user_id", currentUser);
                for (UserBook b: currentBorrowers) {
                    mDataset.add(new UserInfoInList(b.getUser()));
                }
            } catch (Exception e) {
                Log.e("UserBook exception", e.getMessage());
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
