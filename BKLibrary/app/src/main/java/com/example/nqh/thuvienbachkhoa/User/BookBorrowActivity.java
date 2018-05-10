package com.example.nqh.thuvienbachkhoa.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.UserBook;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class BookBorrowActivity extends AppCompatActivity {
    String email="1512395@hcmut.edu.vn";
    ListView ms_lv;
    ms_adapter adapter;
    ArrayList<msBookInfor> arrayList=new ArrayList<msBookInfor>();
    DBHelper database;
    public List<UserBook> UBlist = new Vector<UserBook>();//List UB this page need
    public GeneralUser user =new GeneralUser();
    Button btn_muon;

    public void setDatabase(DBHelper db) {
        this.database = db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muon_sach);

        database = new DBHelper(this);
        ms_lv=(ListView)findViewById(R.id.ms_lv);
        loadData();


        adapter=new ms_adapter(this,arrayList,database);
        ms_lv.setAdapter(adapter);

        btn_muon=(Button)findViewById(R.id.ms_btMuon);
        btn_muon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (UserBook b : UBlist) {
                    try {
                        b.setBorrowed_date(new Date());
                        database.createOrUpdate(b);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(BookBorrowActivity.this,"Mượn sách thành công",Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void loadData() {
        try {
            Map<String, Object> condition1 = new HashMap<String, Object>();
            condition1.put("email", email);
            user = database.queryFirst(GeneralUser.class, condition1);


            Map<String, Object> condition2 = new HashMap<String, Object>();
            condition2.put("user_id", user.getId());
            condition2.put("borrowed_date",0);
            UBlist = database.query(UserBook.class, condition2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (UserBook b : UBlist) {
            Book bn=new Book();
            bn=b.getBook();
            try {
                msBookInfor l = new msBookInfor(bn.getName(),R.drawable.bookex,b.getId());
                arrayList.add(l);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
