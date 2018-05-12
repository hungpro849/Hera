package com.example.nqh.thuvienbachkhoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.User;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by NQH on 27/04/2018.
 */

public class dangkyActivity extends AppCompatActivity implements View.OnClickListener {
    TextView dangnhap;
    Button dangky;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);
        dangnhap=findViewById(R.id.twDangnhap);
        dangky=findViewById(R.id.btnDangky);
        dangnhap.setOnClickListener(this);
        dangky.setOnClickListener(this);
        db =new DBHelper(this);
    }
    public  static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++){
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.twDangnhap:
                Intent modangnhap=new Intent(this,DangNhapActivity.class);
                startActivity(modangnhap);
                break;
            case R.id.btnDangky:
                EditText email,password,hoten,diachi,sodienthoai;
                email=findViewById(R.id.edtEmail);
                password=findViewById(R.id.edtPassword);
                hoten=findViewById(R.id.edtHoten);
                diachi=findViewById(R.id.edtDiachi);
                sodienthoai=findViewById(R.id.edtSodienthoai);
                Boolean checkemail=isValidEmail(email.getText().toString());


                if (checkemail==false  || password.getText().length()==0 ||  hoten.getText().length()==0 ||
                        diachi.getText().length()==0 || sodienthoai.getText().length()==0)
                    TastyToast.makeText(this, "Vui lòng nhập đủ dữ liệu và đúng định dạng email !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                else if(password.getText().length()>15 || password.getText().length()<6)
                {

                    TastyToast.makeText(this, "Password phải từ 6-15 kí tự ", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    password.requestFocus();
                }
                else if(!PhoneNumberUtils.isGlobalPhoneNumber(sodienthoai.getText().toString()))
                {
                    TastyToast.makeText(this, "Số điện thoại không hợp lệ ", Toast.LENGTH_SHORT,TastyToast.ERROR);
                    sodienthoai.requestFocus();
                }


                else
                {
                    String newpassword = null;
                    try {
                        newpassword=computeHash(password.getText().toString());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    User member=new User();
                    member.setAddress(diachi.getText().toString());
                    member.setEmail(email.getText().toString());
                    member.setName(hoten.getText().toString());
                    member.setPassword(newpassword);
                    member.setPhone(sodienthoai.getText().toString());
                    member.setIsuser(1);
                    Map<String, Object> condition = new HashMap<String, Object>();
                    condition.put("email", member.getEmail());
                    List<GeneralUser> founduser = null;
                    try {
                        founduser = db.query(GeneralUser.class, condition);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (founduser.size()>0)
                        TastyToast.makeText(this, "Email đã được đăng ký ", Toast.LENGTH_SHORT,TastyToast.ERROR);
                    else{
                        try {
                            db.fillObject(GeneralUser.class,member);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                         final SweetAlertDialog alert=new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);




                                alert.setTitleText("Congrat!")
                                .setContentText("Tài khoản đã được đăng ký thành công")

                                .show();
                        alert.findViewById(R.id.confirm_button).setVisibility(View.GONE);

                        final Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                alert.dismiss();
                                t.cancel();
                                Intent modangnhap=new Intent(dangkyActivity.this,DangNhapActivity.class);
                                startActivity(modangnhap);
                            }
                        }, 2000);


                    }
                }



        }
    }

}
