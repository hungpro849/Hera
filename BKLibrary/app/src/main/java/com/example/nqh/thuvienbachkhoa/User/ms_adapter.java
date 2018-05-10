package com.example.nqh.thuvienbachkhoa.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.UserBook;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.ArrayList;
import java.util.List;

public class ms_adapter extends BaseAdapter{
    //variable
    Context mContext;
    LayoutInflater inflater;
    List<msBookInfor> modelList;
    ArrayList<msBookInfor> arrayList;
    ImageView del;
    DBHelper database;




    public ms_adapter(Context context, List<msBookInfor> items,DBHelper db){
        mContext=context;
        this.modelList=items;
        inflater=LayoutInflater.from(mContext);
        this.arrayList=new ArrayList<msBookInfor>();
        this.arrayList.addAll(modelList);
        this.database=db;
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
    public View getView(final int position, View view, ViewGroup parent){
        ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view = inflater.inflate(R.layout.activity_line_muon_sach,null);

            holder.nameB=view.findViewById(R.id.ms_name);
            holder.imgB=view.findViewById(R.id.ms_img);
            view.setTag(holder);
        }
        else{
            holder=(ViewHolder) view.getTag();
        }
        holder.nameB.setText(modelList.get(position).getName());
        holder.imgB.setImageResource(modelList.get(position).getIcon());
        del=view.findViewById(R.id.ms_delete);
        del.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    database.deleteById(UserBook.class, modelList.get(position).idOfUserBook);
                    modelList.remove(position);
                    notifyDataSetChanged();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        this.notifyDataSetChanged();
        return view;
    }

}
