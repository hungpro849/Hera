package com.example.nqh.thuvienbachkhoa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Admin.AdminActivity;
import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Interface.LoginAPI;
import com.example.nqh.thuvienbachkhoa.Model.TokenResponse;
import com.example.nqh.thuvienbachkhoa.User.UserActivity;
import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DangNhapActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DangNhapActivity.class.getName();
    TextView dangky,quenmatkhau;
    ListView listview;
    Button dangnhap,khachvanglai;
    EditText email,password;
    DBHelper db;
    LoginAPI loginService;
    SharedPreferences mPrefs;
    ProgressDialog mProgress;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.lwThongtincanhan);
        dangky = findViewById(R.id.twDangky);
        quenmatkhau = findViewById(R.id.twQuenmatkhau);
        dangnhap = findViewById(R.id.btnDangnhap);
        khachvanglai = findViewById(R.id.btnKhachvanglai);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);

        db=new DBHelper(this);
        dangky.setOnClickListener(this);
        quenmatkhau.setOnClickListener(this);
        dangnhap.setOnClickListener(this);
        khachvanglai.setOnClickListener(this);
        mPrefs = getSharedPreferences("mPrefs",MODE_PRIVATE);
        gson = new Gson();

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loginService = retrofit.create(LoginAPI.class);

        String json = mPrefs.getString("UserToken", null);

        if(json != null) {
            TokenResponse tokenResponse = gson.fromJson(json, TokenResponse.class);
            if(tokenResponse.getUser().isAdmin()) {
                Intent adminIntent = new Intent(getApplicationContext(),AdminActivity.class);
                startActivity(adminIntent);
            } else {
                Intent userIntent = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(userIntent);
            }
            Toast.makeText(this, "Chào mừng trở lại " + tokenResponse.getUser().getUsername(), Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.twDangky:
                Intent modangky=new Intent(this,dangkyActivity.class);
                startActivity(modangky);
                break;
            case R.id.twQuenmatkhau:
                Intent moquenmatkhau=new Intent(this,quenmatkhauActivity.class);
                startActivity(moquenmatkhau);
                break;
            case R.id.btnDangnhap:
                if (password.getText().length()==0 )
                    Toast.makeText(this,"Vui lòng nhập đủ dữ liệu và đúng định dạng email !",Toast.LENGTH_LONG).show();
                else {
                    mProgress = new ProgressDialog(this); // this = YourActivity
                    mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgress.setMessage(getString(R.string.login_message));
                    mProgress.setIndeterminate(true);
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                    String mIdentifier = email.getText().toString();
                    String mPassword = password.getText().toString();

                    Call<TokenResponse> tokenResponseCall = loginService.doLogin(mIdentifier, mPassword);

                    tokenResponseCall.enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            int statusCode = response.code();
                            if(statusCode == HttpsURLConnection.HTTP_OK) {
                                mProgress.dismiss();
                                TokenResponse tokenResponse = response.body();
                                Log.d(TAG, "onResponse: " + statusCode + " Username: " + tokenResponse.getUser().getUsername());



                                // Save user data to SharedPreferences
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                String json = gson.toJson(tokenResponse);
                                prefsEditor.putString("UserToken", json);
                                prefsEditor.apply();

                                // Go to different activity based on role
                                if(tokenResponse.getUser().isAdmin()) {
                                    Intent adminIntent = new Intent(getApplicationContext(),AdminActivity.class);
                                    startActivity(adminIntent);
                                } else {
                                    Intent userIntent = new Intent(getApplicationContext(),UserActivity.class);
                                    startActivity(userIntent);
                                }
                                finish();



                            } else {
                                Toast.makeText(getApplicationContext(), R.string.error_login_message, Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                            mProgress.dismiss();
                            Log.d(TAG, "onFailure: " + t.getMessage());

                        }
                    });
                }

                break;
            case R.id.btnKhachvanglai:
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}


