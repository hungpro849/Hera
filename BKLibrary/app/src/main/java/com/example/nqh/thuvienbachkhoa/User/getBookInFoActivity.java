package com.example.nqh.thuvienbachkhoa.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.Database.models.UserBook;
import com.example.nqh.thuvienbachkhoa.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class getBookInFoActivity extends Activity {

    Button bookBorrow;
    String email;
    TextView title, author, numberBook, subject, description, voters, remain;
    ImageView image_book;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_infor_book);
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        description = (TextView) findViewById(R.id.description);
        voters = (TextView) findViewById(R.id.voters);
        remain = (TextView) findViewById(R.id.remain);
        image_book = (ImageView) findViewById(R.id.image_book) ;
        subject = (TextView) findViewById(R.id.subject);

        loadBookInfomation();

//        bookBorrow = (Button)findViewById(R.id.borrowBook);
//        bookBorrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //muonSach();
//                Toast.makeText(getBookInFoActivity.this,"Đã thêm s",Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }
    public  void loadBookInfomation() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email = bundle.getString("email");
        title.setText(bundle.getString("title"));
        author.setText(bundle.getString("author"));
        description.setText(bundle.getString("description"));
        remain.setText(bundle.getString("remain"));
        Picasso.with(getApplicationContext())
                .load(bundle.getString("image_url"))
                .resize(160, 240)
                .into(image_book);
        subject.setText(bundle.getString("subject"));

        if(image_book.getDrawable() == null) {
            image_book.setImageResource(R.drawable.bookex);
        }


    }

//    public void muonSach(){
//        GeneralUser user=new GeneralUser();
//        Book book=new Book();
//        try {
//            Map<String, Object> condition1 = new HashMap<String, Object>();
//            condition1.put("email", email);
//            user = database.queryFirst(GeneralUser.class, condition1);
//
//            Map<String,Object> condition2=new HashMap<String,Object>();
//            condition2.put("name",nb);
//            book=database.queryFirst(Book.class,condition2);
//            UserBook adđB=new UserBook(user,book);
//            database.fillObject(UserBook.class,adđB);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
