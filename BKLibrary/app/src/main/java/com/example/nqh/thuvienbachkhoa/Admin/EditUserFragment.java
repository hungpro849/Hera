package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.User;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class EditUserFragment extends Fragment {
    public Toolbar mToolbar;
    EditText mUserName;
    EditText mUserEmail;
    EditText mUserAddress;
    EditText mUserPhone;
    EditText mUserFName;
    int mRemain;

    CallAPI editUser;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.edit_book_tool_bar);

        mUserName = (EditText) view.findViewById(R.id.edit_user_name);
        mUserEmail = (EditText) view.findViewById(R.id.edit_user_email);
        mUserAddress = (EditText) view.findViewById(R.id.edit_user_address);
        mUserPhone = (EditText) view.findViewById(R.id.edit_user_phone);
        mUserFName = (EditText) view.findViewById(R.id.edit_user_fname);


        mToolbar.setTitle(null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        editUser = retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = this.getActivity().getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);
        setupToolbar();
        setupEditButton();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        setupView();
    }
    public void setDatabase(DBHelper db) {
        //this.database = db;
    }
    public void setupView()
    {
        mUserName.setText(UserListAdapter.currentUser.getUsername());
        mUserEmail.setText(UserListAdapter.currentUser.getEmail());
        mUserAddress.setText(UserListAdapter.currentUser.getAddress());
        mUserPhone.setText(UserListAdapter.currentUser.getPhone());
        mUserFName.setText(UserListAdapter.currentUser.getFullname());
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
    public void setupEditButton() {
        mToolbar.inflateMenu(R.menu.add_book_menu_item);
        mToolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String uId=UserListAdapter.currentUser.getId();
                String uUserName = mUserName.getText().toString().trim();
                String uEmail = mUserEmail.getText().toString().trim();
                String uAddress = mUserAddress.getText().toString().trim();
                String uPhone = mUserPhone.getText().toString().trim();
                String uFName=mUserFName.getText().toString().trim();

                if (!checkEditUserCondition(uUserName,uEmail,uAddress,uPhone,uFName)) {
                    try {
                        Call<User> tokenResponseCall = editUser.editUser("Bearer " + token,uId,uUserName,uEmail,uAddress,uPhone,uFName);
                        tokenResponseCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                //mProgress.dismiss();
                                if (response.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Chỉnh sửa user thành công", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();

                                } else {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        Toast.makeText(getActivity().getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                //mProgress.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Chỉnh sửa user thất bại", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Xin hãy nhập đầy đủ vào các trường", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public boolean checkEditUserCondition(String uUserName, String uEmail, String uAddress, String uPhone, String uFName) {
        //return true if strings are empty or null
        boolean conditionName = TextUtils.isEmpty(uUserName);
        boolean conditionEmail = TextUtils.isEmpty(uEmail);
        boolean conditionAddress = TextUtils.isEmpty(uAddress);
        boolean conditionPhone = TextUtils.isEmpty(uPhone);
        boolean conditionFName = TextUtils.isEmpty(uFName);
        return conditionName || conditionEmail || conditionAddress || conditionPhone ||conditionFName;
    }
}
