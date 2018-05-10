package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.Notification;
import com.example.nqh.thuvienbachkhoa.MainActivity;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationFragment extends Fragment {
    Toolbar mToolbar;
    EditText mNotificationTopic;
    EditText mNotificationContent;
    DBHelper database;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.notification_tool_bar);
        mNotificationContent = (EditText) view.findViewById(R.id.notification_content);
        mNotificationTopic = (EditText) view.findViewById(R.id.notification_topic);
        mToolbar.setTitle(null);

        setupToolbar();
        setupSendButton();
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
    public void setupSendButton() {
        mToolbar.inflateMenu(R.menu.send_notification_menu_item);
        mToolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String topic = mNotificationTopic.getText().toString().trim();
                String content = mNotificationContent.getText().toString().trim();
                if (!checkNewNotificationCondition(topic,content)) {
                    try {
                        Notification newNoti = new Notification(topic,content,new Date());
                        newNoti.setAdmin(getCurrentUser());
                        database.fillObject(Notification.class,newNoti);
                        Toast.makeText(getActivity(), "Tạo thông báo thành công", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Tạo thông báo thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("Create new noti failed",e.getMessage());
                    }
                } else {
                    Toast.makeText(getActivity(), "Xin hãy nhập đầy đủ vào các trường", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public boolean checkNewNotificationCondition(String topic, String content) {
        boolean conditionTopic = topic.isEmpty();
        boolean conditionContent = content.isEmpty();
        return conditionTopic || conditionContent;
    }

    public GeneralUser getCurrentUser () {
        SharedPreferences session = getActivity().getSharedPreferences("sessionUser", Context.MODE_PRIVATE);
        String currentEmail = session.getString("email", "");
        if (!currentEmail.isEmpty()) {
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("email", currentEmail);
            List<GeneralUser> currentUsers = Collections.emptyList();
            try {
                currentUsers = database.query(GeneralUser.class, condition);
                if (currentUsers.size() > 0) {
                    return currentUsers.get(0);
                }
            } catch (Exception e) {
                Log.e("Get current user failed", e.getMessage());
            }
        }
        return null;
    }
}
