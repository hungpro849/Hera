package com.example.nqh.thuvienbachkhoa.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.UserBook;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BorrowBookHistoryActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<LSMS_info> mang_sach;
    DBHelper database;
    public List<UserBook> UBlist = new Vector<UserBook>();
    public GeneralUser user =new GeneralUser();

    public void setDatabase(DBHelper db) {
        this.database = db;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_muon_sach);

        database = new DBHelper(this);
        lv = (ListView) findViewById(R.id.LSMS_lv);

        mang_sach = new ArrayList<LSMS_info>();
        loadData();
        LSMS_list_adapter adapter =new LSMS_list_adapter(
                BorrowBookHistoryActivity.this,
                R.layout.activity_line_lich_su_muon_sach,
                mang_sach
        );

        lv.setAdapter(adapter);


    }

    public void loadData() {
        try {
            Map<String, Object> condition1 = new HashMap<String, Object>();
            condition1.put("email", "1512395@hcmut.edu.vn");
            user = database.queryFirst(GeneralUser.class,condition1);

            Map<String, Object> condition2 = new HashMap<String, Object>();
            condition2.put("user_id",user.getId());
            UBlist=database.query(UserBook.class,condition2);
        } catch (Exception e) {
            // Toast.makeText(BookReturnActivity.this,"something wrong",Toast.LENGTH_SHORT).show();
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
            if(st_s.compareToIgnoreCase("1970")==1) {
                try {
                    LSMS_info l = new LSMS_info(bn.getName(), sm_t, st_t);
                    mang_sach.add(l);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
