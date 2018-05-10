package com.example.nqh.thuvienbachkhoa.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.Dialog;
import android.widget.Toast;


import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.UserBook;
import com.example.nqh.thuvienbachkhoa.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BookReturnActivity extends AppCompatActivity {
    ListView sdm_lv;
    Button btn_trasach;
    ArrayList<SDM_info> L_sdm;
    DBHelper database;
    public List<Book> mBookList = new Vector<Book>();
    public List<UserBook> UBlist = new Vector<UserBook>();
    public List<UserBook> UBlist2 = new Vector<UserBook>();
    public GeneralUser user =new GeneralUser();


    public void setDatabase(DBHelper db) {
        this.database = db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach_dang_muon);
        sdm_lv=(ListView)findViewById(R.id.SDM_lv);

        database = new DBHelper(this);
        L_sdm =new ArrayList<SDM_info>();
        loadData();

        final SDM_list_adapter adapter =new SDM_list_adapter(
                BookReturnActivity.this,
                R.layout.activity_line_sach_dang_muon,
                L_sdm
        );
        sdm_lv.setAdapter(adapter);
        btn_trasach=(Button) findViewById(R.id.SDM_btn_traSach);
        btn_trasach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_nhapcode();
            }
        });
    }

    private void dialog_nhapcode(){
        final Dialog dialog =new Dialog(this);
        dialog.setContentView(R.layout.sdm_nhapcode);
        EditText sdm_code=(EditText)dialog.findViewById(R.id.SDM_tb_code);
        Button oke=(Button)dialog.findViewById(R.id.SDM_btn_xacnhan);
        Button huy=(Button)dialog.findViewById(R.id.SDM_btn_huy);
        oke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (UserBook b : UBlist2) {
                    try {
                        b.setPay_date(new Date());
                        database.createOrUpdate(b);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(BookReturnActivity.this,"Xác nhận trả sách thành công",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void loadData() {
        try {
            Map<String, Object> condition1 = new HashMap<String, Object>();
            condition1.put("email", "1512395@hcmut.edu.vn");
            user = database.queryFirst(GeneralUser.class,condition1);

            Map<String, Object> condition2 = new HashMap<String, Object>();
            condition2.put("user_id",user.getId());
            condition2.put("pay_date",0);
            UBlist=database.query(UserBook.class,condition2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (UserBook b : UBlist) {
            String st=b.getPay_date().toString();
            String st_s=st.substring(30);
            String st_t=st.substring(0,11);
            st_t=st_t.concat(st_s);

            String sm=b.getBorrowed_date().toString();
            String sm_s=sm.substring(30);
            String sm_t=sm.substring(0,11);
            sm_t=sm_t.concat(sm_s);
            Book bn=new Book();
            bn=b.getBook();

            if(st_s.compareToIgnoreCase("1970")==0 && sm_s.compareToIgnoreCase("1970")==1) {
                try {
                    UBlist2.add(b);
                    SDM_info l = new SDM_info(bn.getName(), 1);
                    L_sdm.add(l);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }




}
