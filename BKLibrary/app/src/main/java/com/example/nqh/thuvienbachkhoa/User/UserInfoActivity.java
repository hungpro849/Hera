package com.example.nqh.thuvienbachkhoa.User;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.AccountView;
import com.example.nqh.thuvienbachkhoa.Adapter.CustomAdapterInfoView;
import com.example.nqh.thuvienbachkhoa.DangNhapActivity;
import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.TokenResponse;
import com.example.nqh.thuvienbachkhoa.Model.User;
import com.example.nqh.thuvienbachkhoa.R;
import com.example.nqh.thuvienbachkhoa.doipasswordActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = UserInfoActivity.class.getName();

    TextView doimatkhau;
    ListView listview;
    int[] iconlist={R.drawable.ic_account_circle_black_24dp,R.drawable.ic_email_black_24dp,
            R.drawable.ic_face_black_24dp,R.drawable.ic_person_black_24dp,R.drawable.ic_smartphone_black_24dp,R.drawable.ic_map_black_24dp};
    String[] typelist={"Tên đăng nhập","Email","Loại người dùng","Họ và tên","Số điện thoại","Địa chỉ"};

    List<AccountView> danhsachthongtin;
    String username, email, fullname, address, phone, token;
    CustomAdapterInfoView adapterinfo;
    SharedPreferences mPrefs;
    Gson gson;
    User currentUser;
    ProgressDialog mProgress;
    Button mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences("mPrefs",MODE_PRIVATE);
        gson = new Gson();

        String json = mPrefs.getString("UserData", null);
        token = mPrefs.getString("UserToken", null);

        Log.d(TAG, "Token: " + token);

        if(json != null && token != null) {
            currentUser = gson.fromJson(json, User.class);
            fullname = currentUser.getFullname();
            phone = currentUser.getPhone();
            address = currentUser.getAddress();
            email = currentUser.getEmail();
            username = currentUser.getUsername();
        } else {
            Toast.makeText(this, "Đã có lỗi khi truy vấn thông tin, vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(this, DangNhapActivity.class);
            startActivity(loginIntent);
            finish();
        }

        String[] infolist={username,email,"Sinh viên",fullname,phone,address};
        setContentView(R.layout.thongtincanhan);
        listview=findViewById(R.id.lwThongtincanhan);
        doimatkhau=findViewById(R.id.twDoimatkhau);
        mUpdateButton = findViewById(R.id.btnUpdate);

        doimatkhau.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
        danhsachthongtin = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AccountView accountview = new AccountView();
            accountview.setHinhanh(iconlist[i]);
            accountview.setTypeview(typelist[i]);
            accountview.setInfo(infolist[i]);
            danhsachthongtin.add(accountview);
        }
        adapterinfo = new CustomAdapterInfoView(this, R.layout.customlistview_thongtincanhan,
                danhsachthongtin);
        adapterinfo.notifyDataSetChanged();
        listview.setAdapter(adapterinfo);
        listview.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.twDoimatkhau:
                Intent modoimatkhau=new Intent(this,doipasswordActivity.class);
                startActivity(modoimatkhau);
                break;
            case R.id.btnUpdate:
                mProgress = new ProgressDialog(this); // this = YourActivity
                mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgress.setMessage(getString(R.string.update_message));
                mProgress.setIndeterminate(true);
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                // Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.api_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CallAPI updateService = retrofit.create(CallAPI.class);
                Call<User> tokenResponseCall = updateService.doUpdate("Bearer " + token, currentUser.getPhone(),
                        currentUser.getAddress(), currentUser.getFullname());

                tokenResponseCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()) {
                            mProgress.dismiss();
                            User user = response.body();

                            // Save user data to SharedPreferences
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            String data = gson.toJson(user);
                            prefsEditor.putString("UserData", data);
                            prefsEditor.apply();
                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_LONG ).show();

                        } else {
                            mProgress.dismiss();
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        mProgress.dismiss();
                        Log.d(TAG, "onFailure: " + t.getMessage());

                        Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                });

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view,  int i, long l) {
        if(i>2) {
            final Integer count=i;
            final Dialog customdialog = new Dialog(this);

            customdialog.setCancelable(false);
            customdialog.setContentView(R.layout.customdialog_thongtincanhan);
            final EditText textdieuchinh = customdialog.findViewById(R.id.edtThongtin);
            if (i==4) {
                textdieuchinh.setInputType(InputType.TYPE_CLASS_PHONE);
                textdieuchinh.setText(phone);
            }
            else if(i==3)
                textdieuchinh.setText(fullname);
            else if(i==5)
                textdieuchinh.setText(address);
            Button huy=customdialog.findViewById(R.id.btnHuy);
            Button hoanthanh=customdialog.findViewById(R.id.btnDieuchinh);
            hoanthanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count==3) {
                        currentUser.setFullname(textdieuchinh.getText().toString());
                        danhsachthongtin.get(3).setInfo(textdieuchinh.getText().toString());

                    } else if(count==4) {
                        currentUser.setPhone(textdieuchinh.getText().toString());
                        danhsachthongtin.get(4).setInfo(textdieuchinh.getText().toString());
                    } else if (count==5) {
                        currentUser.setAddress(textdieuchinh.getText().toString());
                        danhsachthongtin.get(5).setInfo(textdieuchinh.getText().toString());
                    }
                    adapterinfo.notifyDataSetChanged();

                    customdialog.cancel();
                }
            });
            huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customdialog.cancel();
                }
            });
            customdialog.show();

        }
    }
}

