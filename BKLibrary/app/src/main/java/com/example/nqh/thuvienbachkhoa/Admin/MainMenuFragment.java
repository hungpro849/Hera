package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.List;
import java.util.Vector;

public class MainMenuFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity mCurrentActivity;

    public void setCurrentActivity(Activity activity) {
        this.mCurrentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_menu_recycler_view);
        setUpRecyclerView();
        return view;
    }

    public void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);

        List<MainMenuTaskInfo> myDataset = new Vector<MainMenuTaskInfo>();

        String[] taskTitles = {"Sách", "Người dùng", "Thông báo", "Báo cáo","Muợn sách"};
        int[] id = {R.drawable.books_icon,R.drawable.users_icon,R.drawable.notifications_icon,
                R.drawable.reports_icon, R.drawable.book_borrow_icon};

        for (int i = 0; i < taskTitles.length; i++) {
            myDataset.add(new MainMenuTaskInfo(taskTitles[i],id[i]));
        }
        mAdapter = new MainMenuAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new GridLayoutManager(mCurrentActivity,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
