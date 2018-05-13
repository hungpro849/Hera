package com.example.nqh.thuvienbachkhoa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.TokenResponse;
import com.example.nqh.thuvienbachkhoa.Model.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NQH on 27/04/2018.
 */

public class doipasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText matkhaucu, matkhaumoi, nhaplai;
    Button hoantat;
    SharedPreferences mPrefs;
    ProgressDialog mProgress;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doipassword);
        matkhaucu = findViewById(R.id.edtMatkhaucu);
        matkhaumoi = findViewById(R.id.edtMatkhaumoi);
        nhaplai = findViewById(R.id.edtNhaplaipass);
        hoantat = findViewById(R.id.btnHoantat);

        hoantat.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHoantat:
                String oldpassword = matkhaucu.getText().toString();
                final String newpassword = matkhaumoi.getText().toString();
                String repassword = nhaplai.getText().toString();
                if (oldpassword.length() == 0 || newpassword.length() == 0 || repassword.length() == 0)
                    Toast.makeText(this, "Vui lòng nhập đủ dữ liệu", Toast.LENGTH_LONG);
                else {
                    if (!newpassword.equals(repassword))
                        Toast.makeText(this, "Mật khẫu mới và nhập lại mật khẩu mới không trùng khớp", Toast.LENGTH_LONG);
                    else if (newpassword.length() < 6 || newpassword.length() > 15)
                        Toast.makeText(this, "Mật khẩu mới phải có 6-15 kí tự", Toast.LENGTH_LONG).show();
                    else {
                        mProgress = new ProgressDialog(this); // this = YourActivity
                        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        mProgress.setMessage("Đang kiểm tra mật khẩu cũ...");
                        mProgress.setIndeterminate(true);
                        mProgress.setCanceledOnTouchOutside(false);
                        mProgress.show();

                        // Get current user data
                        mPrefs = getSharedPreferences("mPrefs",MODE_PRIVATE);
                        gson = new Gson();
                        String dataUser = mPrefs.getString("UserData", null);
                        User user = gson.fromJson(dataUser, com.example.nqh.thuvienbachkhoa.Model.User.class);

                        // Retrofit
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getString(R.string.api_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        final CallAPI service = retrofit.create(CallAPI.class);

                        Call<TokenResponse> tokenResponseCall = service.doLogin(user.getUsername(), oldpassword);

                        tokenResponseCall.enqueue(new Callback<TokenResponse>() {
                            @Override
                            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                                mProgress.dismiss();
                                if(response.isSuccessful()) {
                                    mProgress.setMessage("Đang cập nhật mật khẩu mới...");
                                    mProgress.show();
                                    Call<User> userCall = service.doUpdatePassword("Bearer " + response.body().getJwt(), newpassword);
                                    userCall.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            mProgress.dismiss();
                                            if(response.isSuccessful()) {
                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(doipasswordActivity.this);
                                                builder1.setMessage("Bạn đã đổi mật khẩu thành công");
                                                builder1.setCancelable(true);

                                                builder1.setPositiveButton(
                                                        "Đồng ý",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                finish();
                                                            }
                                                        });

                                                AlertDialog alert11 = builder1.create();
                                                alert11.show();
                                            } else {
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
                                            Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<TokenResponse> call, Throwable t) {
                                mProgress.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                    break;

                }
        }
    }
}
