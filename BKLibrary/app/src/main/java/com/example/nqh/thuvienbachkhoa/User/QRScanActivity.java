package com.example.nqh.thuvienbachkhoa.User;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.example.nqh.thuvienbachkhoa.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class  QRScanActivity extends Activity {
    ImageView qr_img;
    Bundle mBundle;
    Intent mIntent;
    String transaction_id;
    Button mBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        qr_img = findViewById(R.id.qr_code_img);
        mBack = findViewById(R.id.btn_back);

        mIntent = getIntent();
        mBundle = mIntent.getExtras();
        transaction_id = mBundle.getString("transaction_id");
        if(transaction_id.equals("")) {

        }
        else {
            try {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(transaction_id,
                        BarcodeFormat.QR_CODE,800,800);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qr_img.setImageBitmap(bitmap);
            }catch(WriterException e){

                e.printStackTrace();
            }
        }
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(QRScanActivity.this, BorrowBookHistoryActivity.class);
               startActivity(intent);
               finish();
            }
        });
    }

}
