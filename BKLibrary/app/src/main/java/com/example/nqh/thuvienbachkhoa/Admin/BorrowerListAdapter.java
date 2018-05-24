package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.Model.BorrowTransaction;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;
import java.util.List;

public class BorrowerListAdapter extends RecyclerView.Adapter<BorrowerListAdapter.ViewHolder> {
    private List<BorrowTransaction> mBorrowerList ;
    Gson gson;
    SharedPreferences mPrefs;
    String token;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mUser;
        public LinearLayout mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mUser = itemView.findViewById(R.id.borrower_name);
            mTitle = itemView.findViewById(R.id.book_name);
            mItemView = itemView.findViewById(R.id.itemView_borrower);
        }
    }

    public BorrowerListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BorrowerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.borrower_display_in_list, parent, false);

        gson = new Gson();

        BorrowerListAdapter.ViewHolder vh = new BorrowerListAdapter.ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BorrowerListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset.get(position));
        holder.mTitle.setText(mBorrowerList.get(position).getBook().getName());
        holder.mUser.setText(mBorrowerList.get(position).getUser().getUsername());
        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transaction = gson.toJson(mBorrowerList.get(position));
                Intent intent = new Intent(context, ReturnBookActivity.class);
                intent.putExtra("transaction", transaction);
                intent.putExtra("token", token);
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mBorrowerList == null ? 0 : mBorrowerList.size();
    }

    public void setmBorrowerList(List<BorrowTransaction> mBorrowerList) {
        this.mBorrowerList = mBorrowerList;
    }

    public void setToken (String token) {
        this.token = token;
    }

}


