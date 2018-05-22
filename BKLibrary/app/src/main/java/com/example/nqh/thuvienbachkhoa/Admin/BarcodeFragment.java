package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.BorrowTransaction;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarcodeFragment extends Fragment implements BarcodeReader.BarcodeReaderListener {
    private static final String TAG = BarcodeFragment.class.getSimpleName();

    private BarcodeReader barcodeReader;

    ProgressDialog mProgress;

    CallAPI service;
    String token;
    Gson gson;

    public BarcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(CallAPI.class);

        gson = new Gson();

        mProgress = new ProgressDialog(getActivity()); // this = YourActivity
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(getString(R.string.checking_message));
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);

        barcodeReader = (BarcodeReader) getChildFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeReader.setListener(this);
        AdminActivity activity = (AdminActivity) getActivity();
        token = activity.getToken();

        return view;
    }

    @Override
    public void onScanned(final Barcode barcode) {
        barcodeReader.pauseScanning();
        Log.d(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress.show();

                Call<BorrowTransaction> borrowTransactionCall = service.getTransactionByID("Bearer " + token, barcode.displayValue);

                borrowTransactionCall.enqueue(new Callback<BorrowTransaction>() {
                    @Override
                    public void onResponse(Call<BorrowTransaction> call, Response<BorrowTransaction> response) {
                        mProgress.dismiss();
                        if(response.isSuccessful()) {
                            String transaction = gson.toJson(response.body());

                            Intent intent = new Intent(getActivity(), ReturnBookActivity.class);
                            intent.putExtra("transaction", transaction);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BorrowTransaction> call, Throwable t) {
                        mProgress.dismiss();
                        Log.d(TAG, "onFailure: " + t.getMessage());

                        Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_LONG).show();
                    }
                });
                barcodeReader.resumeScanning();
            }
        });

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Log.e(TAG, "onScanError: " + errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getActivity(), "Không có quyền truy cập máy ảnh!", Toast.LENGTH_LONG).show();
    }
}