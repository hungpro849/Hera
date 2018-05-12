package com.example.nqh.thuvienbachkhoa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
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
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.Database.models.Feedback;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.Notification;
import com.example.nqh.thuvienbachkhoa.Database.models.Report;
import com.example.nqh.thuvienbachkhoa.User.UserActivity;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.nqh.thuvienbachkhoa.dangkyActivity.computeHash;
import static com.example.nqh.thuvienbachkhoa.dangkyActivity.isValidEmail;

public class DangNhapActivity extends AppCompatActivity implements View.OnClickListener {
    TextView dangky,quenmatkhau;
    ListView listview;
    Button dangnhap,khachvanglai;
    EditText email,password;
    DBHelper db;
    SharedPreferences session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview=findViewById(R.id.lwThongtincanhan);
        dangky=findViewById(R.id.twDangky);
        quenmatkhau=findViewById(R.id.twQuenmatkhau);
        dangnhap=findViewById(R.id.btnDangnhap);
        khachvanglai=findViewById(R.id.btnKhachvanglai);
        email=findViewById(R.id.edtEmail);
        password=findViewById(R.id.edtPassword);

        db=new DBHelper(this);
        dangky.setOnClickListener(this);
        quenmatkhau.setOnClickListener(this);
        dangnhap.setOnClickListener(this);
        khachvanglai.setOnClickListener(this);

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
                Boolean checkemail=isValidEmail(email.getText().toString());


                if (!checkemail || password.getText().length()==0 )
                    //Toast.makeText(this,"Vui lòng nhập đủ dữ liệu và đúng định dạng email !",Toast.LENGTH_SHORT).show();
                    TastyToast.makeText(this, "Vui lòng nhập đủ dữ liệu và đúng định dạng email !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                else
                {
                    Map<String, Object> condition = new HashMap<String, Object>();
                    condition.put("email", email.getText().toString());
                    List<GeneralUser> founduser = null;
                    try {
                        founduser = db.query(GeneralUser.class, condition);

                    } catch (java.sql.SQLException e) {

                    }
                    if (founduser.size()>0)
                    {

                        String mypassword=null;
                        try {
                            mypassword=computeHash(password.getText().toString());
                        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        if(mypassword.equals(founduser.get(0).getPassword()))
                        {
                            session = this.getSharedPreferences("sessionUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = session.edit();
                            String savedemail = email.getText().toString();
                            editor.putString("email", savedemail);
                            editor.commit();



                            final SweetAlertDialog alert =new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);

                                    alert.setTitleText("Welcome!")
                                    .setContentText("Bạn đã đăng nhập thành công")
                                    .show();
                            alert.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                            final Timer t = new Timer();
                            final List<GeneralUser> finalFounduser = founduser;
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    alert.dismiss();
                                    t.cancel();
                                    if (finalFounduser.get(0).getIsuser() == 0) {
                                        Intent adminIntent = new Intent(DangNhapActivity.this,AdminActivity.class);
                                        startActivity(adminIntent);
                                    }
                                    else
                                    {
                                        Intent testintent = new Intent(DangNhapActivity.this, UserActivity.class);
                                        startActivity(testintent);
                                    }
                                }
                            }, 2000);
                            email.setText("");
                            password.setText("");






                        }
                        else
                            TastyToast.makeText(this, "Email hoặc mật khẩu không đúng", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    }
                    else

                        TastyToast.makeText(this, "Email hoặc mật khẩu không đúng", TastyToast.LENGTH_SHORT, TastyToast.ERROR);


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


