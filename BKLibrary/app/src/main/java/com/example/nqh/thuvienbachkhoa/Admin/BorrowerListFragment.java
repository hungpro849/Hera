package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.BorrowTransaction;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class BorrowerListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    ProgressDialog mProgress;
    public List<BorrowTransaction> mDataset = new Vector<>();
    CallAPI service;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrower_list, container, false);
        mRecyclerView = view.findViewById(R.id.borrower_list_recycle_view);
        mToolbar = view.findViewById(R.id.borrower_list_tool_bar);
        mDataset.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = this.getActivity().getSharedPreferences("mPrefs", MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);
        setUpRecyclerView();
        setupToolbar();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AdminActivity.mActionBar.hide();
    }

    public void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new BorrowerListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadData();

    }

    public void loadData() {
        Call<List<BorrowTransaction>> listCall = service.getTransaction("Bearer " + token);
        mProgress = new ProgressDialog(getActivity()); // this = YourActivity
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(getString(R.string.waiting_message));
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        listCall.enqueue(new Callback<List<BorrowTransaction>>() {
            @Override
            public void onResponse(Call<List<BorrowTransaction>> call, Response<List<BorrowTransaction>> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    loatBorrowTransaction(response.body());
                    ((BorrowerListAdapter) mAdapter).setToken(token);
                    ((BorrowerListAdapter) mAdapter).setmBorrowerList(mDataset);
                    mAdapter.notifyDataSetChanged();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getActivity().getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BorrowTransaction>> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loatBorrowTransaction(List<BorrowTransaction> borrowTransactionList) {
        for (BorrowTransaction borrowTransaction : borrowTransactionList) {
            mDataset.add(borrowTransaction);
        }
    }

    public void setupToolbar() {
        mToolbar.setNavigationIcon(R.drawable.back_button_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
