package com.example.nqh.thuvienbachkhoa.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GetBookInfoActivity extends Activity {

    Button bookBorrowBtn;
    String email;
    TextView title, author, subject, description, voters, remain;
    ImageView image_book;
    Intent mIntent;
    Bundle mBundle;
    Gson gson;
    CallAPI service;
    AlertDialog.Builder borrowBookBuilder, alertBuilder, successAlert;
    ProgressDialog mProgress;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_infor_book);
        gson = new Gson();
        setUpDialog();
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        description = (TextView) findViewById(R.id.description);
        voters = (TextView) findViewById(R.id.voters);
        remain = (TextView) findViewById(R.id.remain);
        image_book = (ImageView) findViewById(R.id.image_book) ;
        subject = (TextView) findViewById(R.id.subject);
        mIntent = getIntent();
        mBundle = mIntent.getExtras();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service =  retrofit.create(CallAPI.class);
        bookBorrowBtn = (Button)findViewById(R.id.borrowBook);
        loadBookInformation();
        bookBorrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = borrowBookBuilder.create();
                alert.setTitle("Bạn có muốn mượn cuốn sách này?");
                alert.setMessage(mBundle.getString("title"));
                alert.show();

            }
        });

    }

    public  void setUpDialog(){

        alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        successAlert  = new AlertDialog.Builder(this);
        successAlert.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        borrowBookBuilder = new AlertDialog.Builder(this);
        borrowBookBuilder.setMessage("Bạn có muốn mượn cuốn sách này?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int rmain = 0;
                        try {
                            rmain = Integer.parseInt(remain.getText().toString());
                        }
                        catch (Exception e) {

                        }
                        if(rmain > 0) {
                            muonSach();
                        }
                        else {
                            AlertDialog alert = alertBuilder.create();
                            alert.setTitle(getString(R.string.borrowed_failure));
                            alert.setMessage(getString(R.string.not_enough_book));
                            alert.show();
                        }

                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

    }
    public void loadBookInformation() {
        mProgress = new ProgressDialog(this); // this = YourActivity
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(getString(R.string.loading_book_info));
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        email = mBundle.getString("email");
        title.setText(mBundle.getString("title"));
        author.setText(mBundle.getString("author"));
        description.setText(mBundle.getString("description"));

        Picasso.with(getApplicationContext())
                .load(mBundle.getString("image_url"))
                .resize(160, 240)
                .into(image_book);
        subject.setText(mBundle.getString("subject"));

        if(image_book.getDrawable() == null) {
            image_book.setImageResource(R.drawable.bookex);
        }
        Call<JsonObject> remainCall = service.getRemain(mBundle.getString("id"));
        remainCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mProgress.dismiss();
                if(response.isSuccessful()) {
                    JsonObject remainResponse = response.body();
                    remain.setText(remainResponse.get("stock").toString());

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    remain.setText("0");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                remain.setText("0");
                bookBorrowBtn.setEnabled(false);
            }
        });


    }

    public void muonSach(){
        mProgress = new ProgressDialog(this); // this = YourActivity
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(getString(R.string.sending_request_borrow));
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        String BearerToken = "Bearer " + mBundle.getString("token");
        Call<JSONObject> borrowCall = service.borrowBook(BearerToken, mBundle.getString("id"));
        borrowCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                mProgress.dismiss();


                if(response.isSuccessful()) {

                        AlertDialog alert = successAlert.create();
                        alert.setTitle(getString(R.string.borrowed_success));
                        alert.setMessage("Đã mượn cuốn " + mBundle.getString("title"));
                        alert.show();

                } else {
                    AlertDialog alert = alertBuilder.create();
                    alert.setTitle(getString(R.string.borrowed_failure));
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        alert.setMessage(jObjError.getString("message"));

                    } catch (Exception e) {
                        alert.setMessage( e.getMessage());
                    }
                    alert.show();
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                mProgress.dismiss();
                AlertDialog alert = alertBuilder.create();
                alert.setTitle(getString(R.string.borrowed_failure));
                alert.setMessage(getString(R.string.connection_error));
                alert.show();
            }
        });
    }


}
