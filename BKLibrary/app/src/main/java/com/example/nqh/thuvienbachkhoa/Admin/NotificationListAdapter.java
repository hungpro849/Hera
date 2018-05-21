package com.example.nqh.thuvienbachkhoa.Admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.R;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTopic;
        public TextView mCreateDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mTopic = itemView.findViewById(R.id.notification_topic);
            mCreateDate = itemView.findViewById(R.id.notification_date);
        }

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
