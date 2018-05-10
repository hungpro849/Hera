package com.example.nqh.thuvienbachkhoa.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.List;

public class SDM_list_adapter extends ArrayAdapter<SDM_info>{
    public SDM_list_adapter(Context context, int resource, List<SDM_info> items){
        super(context,resource,items);
    }

    private class  ViewHolder{
        TextView tvName,tvLanGiahan;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        ViewHolder holder;
        if(view==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.activity_line_sach_dang_muon,null);
            holder=new ViewHolder();
            holder.tvName=(TextView)view.findViewById(R.id.MU_line_name);
            holder.tvLanGiahan=(TextView) view.findViewById(R.id.SDM_tV_langiahan);
            view.setTag(holder);
        }
        else {
            holder=(ViewHolder) view.getTag();
        }

        SDM_info p=getItem(position);
        if(p!=null){
            holder.tvName.setText(p.name);
            holder.tvLanGiahan.setText(String.valueOf(p.soLanGiahan));
        }
        return view;
    }
}
