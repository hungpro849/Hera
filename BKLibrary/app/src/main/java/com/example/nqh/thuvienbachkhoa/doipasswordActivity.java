package com.example.nqh.thuvienbachkhoa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    Toast.makeText(this,"Vui lòng nhập đủ dữ liệu",Toast.LENGTH_LONG);
                else
                {
                    if (!newpassword.equals(repassword))
                        Toast.makeText(this,"Mật khẫu mới và nhập lại mật khẩu mới không trùng khớp",Toast.LENGTH_LONG);
                    else if(newpassword.length()<6 || newpassword.length()>15)
                        Toast.makeText(this, "Mật khẩu mới phải có 6-15 kí tự", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(this, "Mật khẩu cũ không trùng khớp", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(this, "Bạn đã đổi mật khẩu thành công !", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;

        }
    }
}
