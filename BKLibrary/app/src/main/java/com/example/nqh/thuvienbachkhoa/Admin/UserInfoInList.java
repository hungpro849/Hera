package com.example.nqh.thuvienbachkhoa.Admin;

import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.User;

public class UserInfoInList {
    int mUserId;
    String mUsername;
    String mUserEmail;

    UserInfoInList(String username, String userEmail) {
        mUsername = username;
        mUserEmail = userEmail;
    }

    UserInfoInList(GeneralUser user) {
        this.mUserId = user.getId();
        this.mUserEmail = user.getEmail();
        this.mUsername = user.getName();
    }
}
