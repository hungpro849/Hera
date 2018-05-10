package com.example.nqh.thuvienbachkhoa.Admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.GeneralUser;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private List<UserInfoInList> mDataset = Collections.emptyList();
    private DBHelper database;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mUsername;
        public TextView mEmail;
        public Button mEditButton;
        public Button mLockButton;
        public Button mDeleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mUsername = (TextView) itemView.findViewById(R.id.user_name);
            mEmail = (TextView) itemView.findViewById(R.id.user_email);
            mEditButton = (Button) itemView.findViewById(R.id.edit_user_button);
            mLockButton = (Button) itemView.findViewById(R.id.lock_user_button);
            mDeleteButton = (Button) itemView.findViewById(R.id.delete_user_button);
        }

    }

    public UserListAdapter(List<UserInfoInList> myDataset, DBHelper db) {
        mDataset = myDataset;
        database = db;
    }


    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_display_in_list, parent, false);

        UserListAdapter.ViewHolder vh = new UserListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final UserListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset.get(position));
        holder.mUsername.setText(mDataset.get(position).mUsername);
        holder.mEmail.setText(mDataset.get(position).mUserEmail);

        try {
            GeneralUser user = database.getById(GeneralUser.class, mDataset.get(position).mUserId);
            if (user.isIslocked()) {
                holder.mLockButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.lock_icon,0,0,0);
            } else {
                holder.mLockButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.lock_open_icon,0,0,0);
            }
        } catch (Exception e){
        }
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    database.deleteById(GeneralUser.class, mDataset.get(position).mUserId);
                    mDataset.remove(position);
                    notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        });

        holder.mLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GeneralUser user = database.getById(GeneralUser.class, mDataset.get(position).mUserId);
                    user.setIslocked();
                    if (user.isIslocked())
                        holder.mLockButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.lock_icon,0,0,0);
                    else holder.mLockButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.lock_open_icon,0,0,0);
                    database.createOrUpdate(user);
                } catch (Exception e) {
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
