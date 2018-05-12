package com.example.nqh.thuvienbachkhoa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NQH on 27/04/2018.
 */

public class quenmatkhauActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = quenmatkhauActivity.class.getName();
    TextView dangnhap;
    Button khoiphuc;
    EditText email;
    ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quenmatkhau);
        dangnhap=findViewById(R.id.twDangnhapfromquenmatkhau);
        khoiphuc=findViewById(R.id.btnKhoiphuc);
        email=findViewById(R.id.edtEmail);
        dangnhap.setOnClickListener(this);
        khoiphuc.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.twDangnhapfromquenmatkhau:
                Intent modangnhap=new Intent(this,DangNhapActivity.class);
                startActivity(modangnhap);
                break;
            case R.id.btnKhoiphuc:
                Boolean checkemail = Utils.isValidEmail(email.getText().toString());
                if (!checkemail)
                    Toast.makeText(this,"Vui lòng nhập đúng định dạng email !",Toast.LENGTH_LONG).show();
                else {
                    mProgress = new ProgressDialog(this); // this = YourActivity
                    mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgress.setMessage(getString(R.string.waiting_message));
                    mProgress.setIndeterminate(true);
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                    String mEmail = email.getText().toString();
                    // Retrofit
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(getString(R.string.api_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    CallAPI service = retrofit.create(CallAPI.class);

                    Call<JSONObject> jsonObjectCall = service.doResetPassword(mEmail,getText(R.string.api_url).toString());
                    jsonObjectCall.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            mProgress.dismiss();
                            if (response.isSuccessful()) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(quenmatkhauActivity.this);
                                builder1.setMessage("Mã code để đặt lại password đã được gửi tới thư điện tử của bạn");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Đồng ý",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
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
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            mProgress.dismiss();
                            Log.d(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                        }
                    });

                }
                break;


        }
    }
}
