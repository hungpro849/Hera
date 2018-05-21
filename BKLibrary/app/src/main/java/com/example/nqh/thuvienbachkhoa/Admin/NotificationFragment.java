package com.example.nqh.thuvienbachkhoa.Admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.R;

public class NotificationFragment extends Fragment {
    Toolbar mToolbar;
    EditText mNotificationTopic;
    EditText mNotificationContent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mToolbar = view.findViewById(R.id.notification_tool_bar);
        mNotificationContent = view.findViewById(R.id.notification_content);
        mNotificationTopic = view.findViewById(R.id.notification_topic);
        mToolbar.setTitle(null);

        setupToolbar();
        setupSendButton();
        return view;
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
}
