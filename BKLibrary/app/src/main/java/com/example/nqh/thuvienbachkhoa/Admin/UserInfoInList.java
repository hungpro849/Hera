package com.example.nqh.thuvienbachkhoa.Admin;


import com.example.nqh.thuvienbachkhoa.Model.User;

public class UserInfoInList {
    String mUserId;
    String mUsername;
    String mUserEmail;

    UserInfoInList(String username, String userEmail) {
        mUsername = username;
        mUserEmail = userEmail;
    }

    UserInfoInList(User user) {
        this.mUserId = user.getId();
        this.mUserEmail = user.getEmail();
        this.mUsername = user.getUsername();
    }
}
