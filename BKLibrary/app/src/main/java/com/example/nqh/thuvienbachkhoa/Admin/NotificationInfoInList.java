package com.example.nqh.thuvienbachkhoa.Admin;

public class NotificationInfoInList {
    int mNotificationID;
    String mTopic;
    String mDate;

    NotificationInfoInList(String topic, String date) {
        this.mDate = date;
        this.mTopic = topic;
    }

}
