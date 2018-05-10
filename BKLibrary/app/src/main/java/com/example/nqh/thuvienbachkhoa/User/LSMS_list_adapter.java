package com.example.nqh.thuvienbachkhoa.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.List;

public class LSMS_list_adapter extends ArrayAdapter<LSMS_info> {

    public LSMS_list_adapter(Context context, int resource, List<LSMS_info> items){
        super(context,resource,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view =convertView;
        if (view==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.activity_line_lich_su_muon_sach,null);

        }
        LSMS_info p=getItem(position);
        if(p!=null){
            TextView tt1=(TextView) view.findViewById(R.id.ms_name);
            tt1.setText(p.name);
            TextView tt2=(TextView) view.findViewById(R.id.LSMS_tv_ngaymuon);
            tt2.setText(p.ngay_muon);
            TextView tt3=(TextView) view.findViewById(R.id.LSMS_tv_ngaytra);
            tt3.setText(p.ngay_tra);
        }
        return view;
    }



}
