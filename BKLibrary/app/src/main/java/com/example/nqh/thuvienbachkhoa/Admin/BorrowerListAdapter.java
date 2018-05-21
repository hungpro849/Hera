package com.example.nqh.thuvienbachkhoa.Admin;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.List;

public class BorrowerListAdapter extends RecyclerView.Adapter<BorrowerListAdapter.ViewHolder>{
    private List<UserInfoInList> mDataset = Collections.emptyList();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mUsername;
        public TextView mEmail;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            mUsername = (TextView) itemView.findViewById(R.id.borrower_name);
            mEmail = (TextView) itemView.findViewById(R.id.borrower_email);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    @Override
    public BorrowerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.borrower_display_in_list, parent, false);

        BorrowerListAdapter.ViewHolder vh = new BorrowerListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final BorrowerListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 0;
    }
}
