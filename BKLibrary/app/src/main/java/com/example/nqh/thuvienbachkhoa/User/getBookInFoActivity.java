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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class getBookInFoActivity extends Activity {

    Button bookBorrow;
    String email;
    TextView title, author, subject, description, voters, remain;
    ImageView image_book;
    Intent mIntent;
    Bundle mBundle;
    Gson gson;



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
        mIntent = getIntent();
        mBundle = mIntent.getExtras();
        loadBookInfomation();

        bookBorrow = (Button)findViewById(R.id.borrowBook);
        bookBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //muonSach();
                Toast.makeText(getBookInFoActivity.this,"Đang mượn sách",Toast.LENGTH_SHORT).show();

            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public  void loadBookInfomation() {

        email = mBundle.getString("email");
        title.setText(mBundle.getString("title"));
        author.setText(mBundle.getString("author"));
        description.setText(mBundle.getString("description"));
        remain.setText(mBundle.getString("remain"));
        Picasso.with(getApplicationContext())
                .load(mBundle.getString("image_url"))
                .resize(160, 240)
                .into(image_book);
        subject.setText(mBundle.getString("subject"));

        if(image_book.getDrawable() == null) {
            image_book.setImageResource(R.drawable.bookex);
        }


    }

    public void muonSach(){

    }

}
