package com.example.nqh.thuvienbachkhoa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.TokenResponse;
import com.example.nqh.thuvienbachkhoa.User.UserActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NQH on 27/04/2018.
 */

public class dangkyActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = dangkyActivity.class.getName();
    TextView dangnhap;
    Button dangky;
    ProgressDialog mProgress;
    CallAPI signupService;
    EditText email, password, hoten, diachi, sodienthoai, username;
    SharedPreferences mPrefs;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);
        dangnhap = findViewById(R.id.twDangnhap);
        dangky = findViewById(R.id.btnDangky);
        dangnhap.setOnClickListener(this);
        dangky.setOnClickListener(this);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        hoten = findViewById(R.id.edtHoten);
        diachi = findViewById(R.id.edtDiachi);
        sodienthoai = findViewById(R.id.edtSodienthoai);
        username = findViewById(R.id.edtUsername);
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        signupService = retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = getSharedPreferences("mPrefs",MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.twDangnhap:
                Intent modangnhap=new Intent(this,DangNhapActivity.class);
                startActivity(modangnhap);
                finish();
                break;
            case R.id.btnDangky:
                Boolean checkemail = Utils.isValidEmail(email.getText().toString());

                // Checking fields
                if (!checkemail  || password.getText().length()==0 ||  hoten.getText().length()==0 ||
                        diachi.getText().length()==0 || sodienthoai.getText().length()==0)
                    Toast.makeText(this,"Vui lòng nhập đủ dữ liệu và đúng định dạng email !",Toast.LENGTH_LONG).show();
                else if(password.getText().length()>15 || password.getText().length()<6) {
                    Toast.makeText(this, "Password phải từ 6-15 kí tự ", Toast.LENGTH_LONG).show();
                    password.requestFocus();
                } else if(!PhoneNumberUtils.isGlobalPhoneNumber(sodienthoai.getText().toString())) {
                    Toast.makeText(this, "Số điện thoại không hợp lệ ", Toast.LENGTH_LONG).show();
                    sodienthoai.requestFocus();
                } else {
                    // Setup progress diaglog view
                    mProgress = new ProgressDialog(this); // this = YourActivity
                    mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgress.setMessage(getString(R.string.signup_message));
                    mProgress.setIndeterminate(true);
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    // Parsing data from form
                    String mUsername = username.getText().toString();
                    String mPassword = password.getText().toString();
                    String mEmail = email.getText().toString();
                    String mAddress = diachi.getText().toString();
                    String mPhone = sodienthoai.getText().toString();
                    String mFullName = hoten.getText().toString();

                    Call<TokenResponse> tokenResponseCall = signupService.doRegister(mEmail, mPassword, mUsername,
                            mAddress, mFullName, mPhone);

                    tokenResponseCall.enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            mProgress.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Đăng ký thành công!", Toast.LENGTH_LONG).show();

                                // Save user data to SharedPreferences
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                String json = gson.toJson(response.body());
                                prefsEditor.putString("UserToken", json);
                                prefsEditor.apply();

                                // Redirect to user dashboard
                                Intent userIntent = new Intent(getApplicationContext(),UserActivity.class);
                                startActivity(userIntent);
                                finish();

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
                            Log.d(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                        }
                    });


                }



        }
    }

}
