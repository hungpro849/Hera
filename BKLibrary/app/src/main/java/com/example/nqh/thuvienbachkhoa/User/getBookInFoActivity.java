package com.example.nqh.thuvienbachkhoa.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


public class getBookInFoActivity extends Activity {

    Button bookBorrow;
    String email,nb;
    TextView nameBook, author, numberBook, subject, description, imageLink, voters, remain;
    public List<Book> mBookList = new Vector<Book>();
    ArrayList<line_main_user_infor> mang_sach = new ArrayList<line_main_user_infor>();
    DBHelper database;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mt("Create Activity2");
        setContentView(R.layout.activity_get_infor_book);
        database = new DBHelper(this);
        nameBook = (TextView) findViewById(R.id.nameBook);
        author = (TextView) findViewById(R.id.author);
        description = (TextView) findViewById(R.id.description);
        voters = (TextView) findViewById(R.id.voters);
        remain = (TextView) findViewById(R.id.remain);
        Intent intent = getIntent();
        Bundle bundel = intent.getExtras();
        nb = bundel.getString("nameBook");
        email=bundel.getString("email");
        nameBook.setText(nb.toString());

        try {
            mBookList = database.getAllOrdered(Book.class, "name", true);
            nameBook.getText();
            nameBook = (TextView) findViewById(R.id.nameBook);
            author = (TextView) findViewById(R.id.author);
            description = (TextView) findViewById(R.id.description);
            voters = (TextView) findViewById(R.id.voters);
            remain = (TextView) findViewById(R.id.remain);
            for (Book b : mBookList) {
                if (b.getName().equals(nameBook.getText())) {
                    author.setText(b.getAuthor());
                    remain.setText("Số Lượng Sách: " + b.getRemain());
                    voters.setText("Đánh Giá: " + b.getVoters());
                    description.setText(b.getDescription());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        bookBorrow=(Button)findViewById(R.id.borrowBook);
        bookBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                muonSach();
                Toast.makeText(getBookInFoActivity.this,"Đã thêm s",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void muonSach(){
        GeneralUser user=new GeneralUser();
        Book book=new Book();
        try {
            Map<String, Object> condition1 = new HashMap<String, Object>();
            condition1.put("email", email);
            user = database.queryFirst(GeneralUser.class, condition1);

            Map<String,Object> condition2=new HashMap<String,Object>();
            condition2.put("name",nb);
            book=database.queryFirst(Book.class,condition2);
            UserBook adđB=new UserBook(user,book);
            database.fillObject(UserBook.class,adđB);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
