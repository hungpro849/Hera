package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.BorrowTransaction;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReturnBookActivity extends AppCompatActivity {
    private static final String TAG = BarcodeFragment.class.getSimpleName();

    Gson gson;
    TextView title, author, subject, email, name, date;
    ImageView cover;
    Button returrnBook, backButton;
    CallAPI service;
    ProgressDialog mProgress;
    String token;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);

        gson = new Gson();

        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        subject = findViewById(R.id.subject);
        email = findViewById(R.id.email);
        name = findViewById(R.id.user_borrow);
        date = findViewById(R.id.borrow_date);
        cover = findViewById(R.id.image_book);
        returrnBook = findViewById(R.id.btn_return_book);
        backButton = findViewById(R.id.btn_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(CallAPI.class);

        mProgress = new ProgressDialog(this); // this = YourActivity
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(getString(R.string.checking_message));
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(false);

        alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        finish();
                    }
                });

        String transaction = getIntent().getStringExtra("transaction");
        token = getIntent().getStringExtra("token");
        BorrowTransaction mTransaction = gson.fromJson(transaction, BorrowTransaction.class);
        loadData(mTransaction, this);

    }

    private void loadData(BorrowTransaction borrowTransaction, final Context context) {
        final String id = borrowTransaction.getId();
        title.setText(borrowTransaction.getBook().getName());
        author.setText(borrowTransaction.getBook().getAuthor());
        subject.setText(borrowTransaction.getBook().getSubject());
        email.setText(borrowTransaction.getUser().getEmail());
        name.setText(borrowTransaction.getUser().getUsername());
        Picasso.with(this).load(borrowTransaction.getBook().getImageLink()).resize(120, 160).into(cover);
        if(cover.getDrawable() == null) {
            cover.setImageResource(R.drawable.bookex);
        }
        returrnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                Call<JsonObject> jsonObjectCall = service.returnBook("Bearer " + token, id);
                jsonObjectCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        mProgress.dismiss();
                        if(response.isSuccessful()) {
                            AlertDialog alertDialog = alertBuilder.create();
                            alertDialog.setMessage("Trả sách thành công");
                            alertDialog.show();
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        mProgress.dismiss();
                        Log.d(TAG, "onFailure: " + t.getMessage());

                        Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
