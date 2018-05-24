package com.example.nqh.findjobs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner optionselect=findViewById(R.id.spinnerFrom);

        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this,
                R.array.OptionSelect, android.R.layout.simple_spinner_item);

        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionselect.setAdapter(adapterFrom);
        recyclerView=findViewById(R.id.recyclerViewJob);
        initView();



    }
    public void initView(){

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<job> listjob=new ArrayList<>();
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        listjob.add(new job("abc","123"));
        customadapter jobadapter =new customadapter(listjob,getApplicationContext());
        recyclerView.setAdapter(jobadapter);

    }

}
