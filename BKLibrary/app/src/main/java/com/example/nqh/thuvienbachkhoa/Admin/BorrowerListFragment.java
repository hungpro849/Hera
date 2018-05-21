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

import com.example.nqh.thuvienbachkhoa.R;

import java.util.List;
import java.util.Vector;

public class BorrowerListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private android.support.v7.widget.SearchView mSearchView;
    private List<UserInfoInList> mDataset = new Vector<UserInfoInList>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrower_list, container, false);
        mRecyclerView = view.findViewById(R.id.borrower_list_recycle_view);
        mToolbar = view.findViewById(R.id.borrower_list_tool_bar);
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

}
