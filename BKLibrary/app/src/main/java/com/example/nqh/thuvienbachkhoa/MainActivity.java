package com.example.nqh.thuvienbachkhoa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent dangNhapActivity = new Intent(this, DangNhapActivity.class);
        startActivity(dangNhapActivity);
    }

    //on returning to this activity, which means from any other activity, on back press, finish this
    // since this is just to load and check data.
    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
