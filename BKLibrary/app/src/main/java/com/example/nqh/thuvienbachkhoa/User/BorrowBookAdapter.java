package com.example.nqh.thuvienbachkhoa.User;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.nqh.thuvienbachkhoa.Model.Book;
import com.example.nqh.thuvienbachkhoa.Model.BorrowTransaction;
import com.example.nqh.thuvienbachkhoa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BorrowBookAdapter extends RecyclerView.Adapter {
    private List<BorrowTransaction> mDataSet;
    private LayoutInflater mInflater;
    public Context mContext;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    AlertDialog.Builder alertBuilder;


    public BorrowBookAdapter(Context context, List<BorrowTransaction> dataSet) {
       this.mContext = context;
       this.mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);

        // uncomment if you want to open only one row at a time
        // binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_line_lich_su_muon_sach, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final ViewHolder holder = (ViewHolder) h;

        if (mDataSet != null && 0 <= position && position < mDataSet.size()) {
            final String data = mDataSet.get(position).getId();

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipeLayout, data);

            // Bind your data here
            holder.bind(mDataSet.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private View frontLayout;
        private View deleteLayout;
        private TextView title;
        private  TextView author;
        private ImageView image;
        private TextView subject;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            frontLayout = itemView.findViewById(R.id.front_layout);
            deleteLayout = itemView.findViewById(R.id.reveal_layout);
            title = (TextView) itemView.findViewById(R.id.main_name_book);
            image = (ImageView) itemView.findViewById(R.id.main_line_img);
            author = (TextView) itemView.findViewById(R.id.main_author_book);
            subject = (TextView) itemView.findViewById(R.id.main_subject_book);
        }

        public void bind(final BorrowTransaction data) {
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertBuilder = new AlertDialog.Builder(mContext);
                    alertBuilder.setCancelable(false)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDataSet.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());

                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alertBuilder.create();
                    alert.setTitle("Trả sách");
                    alert.setMessage("Bạn có muốn trả sách?");
                    alert.show();
                }
            });

            Book mBook = data.getBook();
            if(mBook != null) {
                title.setText(data.getBook().getName());
                author.setText(data.getBook().getAuthor());
                subject.setText(data.getBook().getSubject());
                Picasso.with(mContext).load(data.getBook().getImageLink()).resize(120, 160).into(image);
                if (image.getDrawable() == null) {
                    image.setImageResource(R.drawable.bookex);
                }
            }

            frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("click", "click");

                }
            });
        }
    }
}
