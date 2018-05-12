package com.example.nqh.thuvienbachkhoa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.User.UserInfoActivity;
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

/**
 * Created by NQH on 27/04/2018.
 */

public class doipasswordActivity extends AppCompatActivity implements View.OnClickListener {
    DBHelper db;
    EditText matkhaucu,matkhaumoi,nhaplai;
    Button hoantat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doipassword);
        matkhaucu=findViewById(R.id.edtMatkhaucu);
        matkhaumoi=findViewById(R.id.edtMatkhaumoi);
        nhaplai=findViewById(R.id.edtNhaplaipass);
        hoantat=findViewById(R.id.btnHoantat);

        hoantat.setOnClickListener(this);





    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnHoantat:
                String oldpassword=matkhaucu.getText().toString();
                String newpassword=matkhaumoi.getText().toString();
                String repassword=nhaplai.getText().toString();
                if(oldpassword.length()==0 || newpassword.length() == 0 || repassword.length() == 0)
                    TastyToast.makeText(this, "Vui lòng nhập đủ dữ liệu", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                else
                {
                    if (!newpassword.equals(repassword))
                        TastyToast.makeText(this, "Mật khẫu mới và nhập lại mật khẩu mới không trùng khớp", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    else if(newpassword.length()<6 || newpassword.length()>15)
                        TastyToast.makeText(this, "Mật khẩu mới phải có 6-15 kí tự", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    else
                    {
                        db=new DBHelper(this);
                        SharedPreferences session=doipasswordActivity.this.getSharedPreferences("sessionUser",MODE_PRIVATE);
                        String myemail=session.getString("email","");
                        Map<String, Object> condition = new HashMap<String, Object>();
                        condition.put("email",myemail);
                        List<GeneralUser> founduser = null;
                        String hashpassword=null;
                        String newhashpassword=null;
                        try {
                            founduser = db.query(GeneralUser.class, condition);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            hashpassword=computeHash(oldpassword);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (!hashpassword.equals(founduser.get(0).getPassword()))
                            TastyToast.makeText(this, "Mật khẩu cũ không trùng khớp", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        else
                        {
                            try {
                                newhashpassword=computeHash(newpassword);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            founduser.get(0).setPassword(newhashpassword);
                            try {
                                db.createOrUpdate(founduser.get(0));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            final SweetAlertDialog alert=new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                            alert.setTitleText("Congrat!")
                                    .setContentText("Bạn đã đổi mật khẩu thành công !")
                                    .show();
                            alert.findViewById(R.id.confirm_button).setVisibility(View.GONE);
                            final Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    alert.dismiss();
                                    t.cancel();
                                    Intent modangnhap=new Intent(doipasswordActivity.this,UserInfoActivity.class);
                                    startActivity(modangnhap);
                                }
                            }, 2000);
                        }
                    }
                }

                break;

        }
    }
}
