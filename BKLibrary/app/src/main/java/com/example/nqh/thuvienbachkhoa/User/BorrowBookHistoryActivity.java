package com.example.nqh.thuvienbachkhoa.User;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.Book;
import com.example.nqh.thuvienbachkhoa.Model.BorrowTransaction;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BorrowBookHistoryActivity extends AppCompatActivity {
    private List<BorrowTransaction> mDataset = new ArrayList<>();
    private List<BorrowTransaction> mDatasetBackup = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BorrowBookAdapter mBorrowBookAdapter;
    private CallAPI service;
    Gson gson;
    SharedPreferences mPrefs;
    String token;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_book);
        gson = new Gson();
        mPrefs = getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);
        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        getTransactionFromAPI();
        setupRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (mBorrowBookAdapter != null) {
            mBorrowBookAdapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (mBorrowBookAdapter != null) {
            mBorrowBookAdapter.restoreStates(savedInstanceState);
        }
    }

    private void getTransactionFromAPI() {
        mProgress = new ProgressDialog(this); // this = YourActivity
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(getString(R.string.loading_history_transaction));
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service =  retrofit.create(CallAPI.class);
        Call<List<BorrowTransaction>> bookTransaction = service.getBorrowHistory("Bearer " + token);

        bookTransaction.enqueue(new Callback<List<BorrowTransaction>>() {
            @Override
            public void onResponse(Call<List<BorrowTransaction>> call, Response<List<BorrowTransaction>> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    // Save user data to SharedPreferences
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    String result = gson.toJson(response.body());

                    prefsEditor.putString("BorrowHistory", result);
                    prefsEditor.apply();

                    mDataset.addAll(response.body());
                    mBorrowBookAdapter = new BorrowBookAdapter(BorrowBookHistoryActivity.this, mDataset);
                    mRecyclerView.setAdapter(mBorrowBookAdapter);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String backupTransaction = mPrefs.getString("BorrowHistory", null);
                        if(!backupTransaction.equals(null)) {
                            List<BorrowTransaction> backup = gson.fromJson(backupTransaction, new TypeToken<List<BorrowTransaction>>() {
                            }.getType());
                           mDataset.addAll(backup);
                           mBorrowBookAdapter = new BorrowBookAdapter(BorrowBookHistoryActivity.this, mDataset);
                           mRecyclerView.setAdapter(mBorrowBookAdapter);
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<BorrowTransaction>> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                String backupTransaction = mPrefs.getString("BorrowHistory", null);
                if(!backupTransaction.equals(null)) {
                    List<BorrowTransaction> backup = gson.fromJson(backupTransaction, new TypeToken<List<BorrowTransaction>>() {
                    }.getType());
                    mDataset.addAll(backup);
                    mBorrowBookAdapter = new BorrowBookAdapter(BorrowBookHistoryActivity.this, mDataset);
                    mRecyclerView.setAdapter(mBorrowBookAdapter);
                }

            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }


}
