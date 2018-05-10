package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Notification;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.List;
import java.util.Vector;

public class NotificationListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity mCurrentActivity;
    private DBHelper database;
    private Toolbar mToolbar;
    private FloatingActionButton mSwitchToComposBtn;

    public List<NotificationInfoInList> mDataset = new Vector<NotificationInfoInList>();
    public List<Notification> mNotificationList = new Vector<Notification>();

    public void setDatabase(DBHelper db) {
        this.database = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.notification_list_recycle_view);
        mSwitchToComposBtn = (FloatingActionButton) view.findViewById(R.id.switch_to_compose_notification_btn);
        mDataset.clear();
        mNotificationList.clear();


        mToolbar = (Toolbar) view.findViewById(R.id.notification_list_tool_bar);
        setUpRecyclerView();
        setupSwitchToComposeButton();
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

        loadData();

        mAdapter = new NotificationListAdapter(mDataset, database);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(mCurrentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }



    public void loadData() {
        try {
            mNotificationList = database.getAllOrdered(Notification.class, "create_date", true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Notification b : mNotificationList) {
            NotificationInfoInList newNoti = new NotificationInfoInList(b);
            mDataset.add(newNoti);
        }
    }

    public void setupSwitchToComposeButton() {
        mSwitchToComposBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_view, AdminActivity.mNotificationFragment)
                        .addToBackStack("Notification fragment").commit();
            }
        });
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

}
