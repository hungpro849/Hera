package com.example.nqh.findjobs;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by NQH on 23/05/2018.
 */

public class customadapter extends RecyclerView.Adapter<customadapter.ViewHolder> {
    ArrayList<job> listjob;
    static Context context;



    public customadapter(ArrayList<job> listjob, Context context) {
        this.listjob = listjob;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView twtitle,twsalary;
        public ViewHolder(View itemView) {
            super(itemView);
            twsalary=itemView.findViewById(R.id.salary);
            twtitle=itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("abc",""+getAdapterPosition());
        }
        // each data item is just a string in this case




    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.customjobs,parent,false);



        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(customadapter.ViewHolder holder, final int position) {
        holder.twsalary.setText(listjob.get(position).getSalary());
        holder.twtitle.setText(listjob.get(position).getTittle());

    }

    @Override
    public int getItemCount() {
        return listjob.size();
    }
}
