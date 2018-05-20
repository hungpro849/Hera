package com.example.nqh.thuvienbachkhoa.User;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private List<BookInfoView> booksList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imgage;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.main_name_book);
            imgage = (ImageView) view.findViewById(R.id.main_line_img);
        }

    }

    public BooksAdapter(List<BookInfoView> booksList) {
        this.booksList = booksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_line_main_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BookInfoView book = booksList.get(position);
        holder.title.setText(book.getName());
        //holder.imgage.setImageResource(book.getImage());
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }




}
