package com.example.nqh.thuvienbachkhoa.Admin;

import com.example.nqh.thuvienbachkhoa.Database.models.Notification;

public class NotificationInfoInList {
    int mNotificationID;
    String mTopic;
    String mDate;

    NotificationInfoInList(String topic, String date) {
        this.mDate = date;
        this.mTopic = topic;
    }

    NotificationInfoInList(Notification notification) {
        this.mNotificationID = notification.getId();
        this.mTopic = notification.getTopic();
        this.mDate = notification.getCreate_date().toString();
    }
}
