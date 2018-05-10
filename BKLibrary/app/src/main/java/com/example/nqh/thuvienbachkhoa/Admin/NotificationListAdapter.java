package com.example.nqh.thuvienbachkhoa.Admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private List<NotificationInfoInList> mDataset = Collections.emptyList();
    private DBHelper database;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTopic;
        public TextView mCreateDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mTopic = (TextView) itemView.findViewById(R.id.notification_topic);
            mCreateDate = (TextView) itemView.findViewById(R.id.notification_date);
        }

    }

    public NotificationListAdapter(List<NotificationInfoInList> myDataset, DBHelper db) {
        mDataset = myDataset;
        database = db;
    }


    @Override
    public NotificationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_display_in_list, parent, false);

        NotificationListAdapter.ViewHolder vh = new NotificationListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NotificationListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset.get(position));
        holder.mTopic.setText(mDataset.get(position).mTopic);
        holder.mCreateDate.setText(mDataset.get(position).mDate);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
