package com.example.nqh.thuvienbachkhoa.User;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.R;
import com.example.nqh.thuvienbachkhoa.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private List<BookInfoView> booksList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public  TextView author;
        public ImageView image;
        public TextView subject;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.main_name_book);
            image = view.findViewById(R.id.main_line_img);
            author = view.findViewById(R.id.main_author_book);
            subject = view.findViewById(R.id.main_subject_book);
        }

    }

    public BooksAdapter(Context context, List<BookInfoView> booksList) {
        this.context = context;
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
        Picasso.with(context).load(book.getImage()).resize(120, 160).into(holder.image);
        if(holder.image.getDrawable() == null) {
            holder.image.setImageResource(R.drawable.bookex);
        }
        holder.author.setText(book.getAuthor());
        holder.subject.setText(book.getSubject());
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }




}
