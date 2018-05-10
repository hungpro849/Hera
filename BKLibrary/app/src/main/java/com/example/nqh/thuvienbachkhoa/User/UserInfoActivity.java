package com.example.nqh.thuvienbachkhoa.User;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.AccountView;
import com.example.nqh.thuvienbachkhoa.Adapter.CustomAdapterInfoView;
import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.R;
import com.example.nqh.thuvienbachkhoa.doipasswordActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    DBHelper db;

    TextView doimatkhau;
    ListView listview;
    int[] iconlist={R.drawable.email,R.drawable.member,R.drawable.fullname,R.drawable.phone,R.drawable.address};
    String[] typelist={"Email","Loại người dùng","Họ và tên","Số điện thoại","Địa chỉ"};

    List<AccountView> danhsachthongtin;
    String hoten,sodienthoai,diachi;
    List<GeneralUser> founduser=null;
    CustomAdapterInfoView adapterinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DBHelper(this);
        SharedPreferences session=UserInfoActivity.this.getSharedPreferences("sessionUser",MODE_PRIVATE);
        String myemail=session.getString("email","");
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("email",myemail);

        try {
            founduser = db.query(GeneralUser.class, condition);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        hoten=founduser.get(0).getName();
        sodienthoai=founduser.get(0).getPhone();
        diachi=founduser.get(0).getAddress();

        String[] infolist={founduser.get(0).getEmail(),"Sinh viên",founduser.get(0).getName(),founduser.get(0).getPhone(),founduser.get(0).getAddress()};
        setContentView(R.layout.thongtincanhan);
        listview=findViewById(R.id.lwThongtincanhan);
        doimatkhau=findViewById(R.id.twDoimatkhau);

        doimatkhau.setOnClickListener(this);
        danhsachthongtin=new ArrayList<>();
        for (int i=0;i<5;i++)
        {
            AccountView accountview=new AccountView();
            accountview.setHinhanh(iconlist[i]);
            accountview.setTypeview(typelist[i]);
            accountview.setInfo(infolist[i]);
            danhsachthongtin.add(accountview);
        }
        adapterinfo=new CustomAdapterInfoView(UserInfoActivity.this,R.layout.customlistview_thongtincanhan,danhsachthongtin);
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

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view,  int i, long l) {
        if(i>1)
        {



            final Integer count=i;
            final Dialog customdialog=new Dialog(this);


            customdialog.setCancelable(false);
            customdialog.setContentView(R.layout.customdialog_thongtincanhan);
            final EditText textdieuchinh=customdialog.findViewById(R.id.edtThongtin);
            if (i==3)
            {

                textdieuchinh.setInputType(InputType.TYPE_CLASS_PHONE);
                textdieuchinh.setText(sodienthoai);
            }
            else if(i==2)
                textdieuchinh.setText(hoten);
            else if(i==4)
                textdieuchinh.setText(diachi);
            Button huy=customdialog.findViewById(R.id.btnHuy);
            Button hoanthanh=customdialog.findViewById(R.id.btnDieuchinh);
            hoanthanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count==2)
                    {
                        founduser.get(0).setName(textdieuchinh.getText().toString());
                        danhsachthongtin.get(2).setInfo(textdieuchinh.getText().toString());
                    }
                    else if(count==3)
                    {
                        founduser.get(0).setPhone(textdieuchinh.getText().toString());
                        danhsachthongtin.get(3).setInfo(textdieuchinh.getText().toString());
                    }
                    else if (count==4)
                    {
                        founduser.get(0).setAddress(textdieuchinh.getText().toString());
                        danhsachthongtin.get(4).setInfo(textdieuchinh.getText().toString());
                    }
                    try {
                        db.createOrUpdate(founduser.get(0));
                    } catch (SQLException e) {
                        e.printStackTrace();
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

