package com.example.nqh.thuvienbachkhoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.nqh.thuvienbachkhoa.dangkyActivity.computeHash;
import static com.example.nqh.thuvienbachkhoa.dangkyActivity.isValidEmail;

/**
 * Created by NQH on 27/04/2018.
 */

public class quenmatkhauActivity extends AppCompatActivity implements View.OnClickListener {
    TextView dangnhap;
    Button khoiphuc;
    EditText email;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quenmatkhau);
        db=new DBHelper(this);
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
                Boolean checkemail=isValidEmail(email.getText().toString());
                if (!checkemail  )
                    TastyToast.makeText(this,"Vui lòng nhập đúng định dạng email !",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                else
                {
                    Map<String, Object> condition = new HashMap<String, Object>();
                    condition.put("email", email.getText().toString());
                    List<GeneralUser> founduser = null;
                    try {
                        founduser = db.query(GeneralUser.class, condition);

                    } catch (java.sql.SQLException e) {

                    }
                    if (founduser.size()==0)
                        TastyToast.makeText(this,"Email này chưa đăng ký",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    else
                    {

                        final String newpass= GenerateRandomString.randomString(7);
                        String hashpassword = null;
                        try {
                            hashpassword=computeHash(newpass);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        founduser.get(0).setPassword(hashpassword);
                        try {
                            db.createOrUpdate(founduser.get(0));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    GMailSender sender = new GMailSender("bklibrarydemo@gmail.com",
                                            "LamThanhHoang");
                                    sender.sendMail("Reset your password", "Mật khẩu mới của bạn là : "+newpass,
                                            "bklibrarydemo@gmail.com", email.getText().toString());
                                } catch (Exception e) {
                                    Log.e("SendMail", e.getMessage(), e);
                                }
                            }

                        }).start();
                        final SweetAlertDialog alert=new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                        alert.setTitleText("Congrat!")
                                .setContentText("Đã reset password cho tài khoản của bạn")
                                .show();
                        alert.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                        final Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                alert.dismiss();
                                t.cancel();
                                Intent modangnhap=new Intent(quenmatkhauActivity.this,DangNhapActivity.class);
                                startActivity(modangnhap);
                            }
                        }, 2000);
                    }
                }
                break;


        }
    }
}
