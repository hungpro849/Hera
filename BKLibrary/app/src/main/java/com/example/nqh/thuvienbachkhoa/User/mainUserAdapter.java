package com.example.nqh.thuvienbachkhoa.User;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class mainUserAdapter extends BaseAdapter {

    //variable
    Context mContext;
    LayoutInflater inflater;
    List<line_main_user_infor> modelList;
    ArrayList<line_main_user_infor> arrayList;
    String email;

    public mainUserAdapter(Context context, List<line_main_user_infor> items,String em){
        mContext=context;
        this.modelList=items;
        inflater=LayoutInflater.from(mContext);
        this.arrayList=new ArrayList<line_main_user_infor>();
        this.arrayList.addAll(modelList);
        email=em;

    }

    private class ViewHolder{
        TextView nameB;
        ImageView imgB;

    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view=inflater.inflate(R.layout.activity_line_main_user,null);
            //lacate the view in line_main_user
            holder.nameB=view.findViewById(R.id.main_name_book);
            holder.imgB=view.findViewById(R.id.main_line_img);
            view.setTag(holder);
        }
        else {
            holder=(ViewHolder) view.getTag();
        }

        //set the result intc textView
        holder.nameB.setText(modelList.get(position).getName());
        holder.imgB.setImageResource(modelList.get(position).getIcon());

        //Listview item onclick
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //code later
                TextView nameBook=(TextView) view.findViewById(R.id.main_name_book);
                Intent intents = new Intent(mContext, getBookInFoActivity.class);
                intents.putExtra("nameBook", nameBook.getText());
                intents.putExtra("email",email);
                mContext.startActivity(intents);
            }
        });
        return view;
    }

    //filter
    public void filter(String charText){
        charText=charText.toLowerCase(Locale.getDefault());
        modelList.clear();
        if(charText.length()==0){
            modelList.addAll(arrayList);
        }
        else {
            for (line_main_user_infor model:arrayList){
                if (model.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    modelList.add(model);
                }
            }
        }
        this.notifyDataSetChanged();
    }
}
