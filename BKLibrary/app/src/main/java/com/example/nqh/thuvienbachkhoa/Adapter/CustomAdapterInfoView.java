package com.example.nqh.thuvienbachkhoa.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.AccountView;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.List;

/**
 * Created by NQH on 24/04/2018.
 */

public class CustomAdapterInfoView extends BaseAdapter{
    Context mycontext;
    int myresource;
    List<AccountView> accountViewList;
    public CustomAdapterInfoView(@NonNull Context context, int resource, @NonNull List<AccountView> objects) {

        mycontext=context;
        myresource=resource;
        accountViewList=objects;
    }

    @Override
    public int getCount() {
        return accountViewList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(myresource,null);
        ImageView iconrow=convertView.findViewById(R.id.iwInforicon);
        TextView typerow=convertView.findViewById(R.id.twHOTEN);
        TextView inforow=convertView.findViewById(R.id.twTEN);
        iconrow.setImageResource(accountViewList.get(position).getHinhanh());
        typerow.setText(accountViewList.get(position).getTypeview());
        inforow.setText(accountViewList.get(position).getInfo());
        return convertView;


    }
}
