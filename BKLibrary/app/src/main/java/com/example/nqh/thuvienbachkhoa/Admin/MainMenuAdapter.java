package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.ClipData;
import android.content.res.Resources;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.R;

import java.util.List;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {
    private List<MainMenuTaskInfo> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTitle;
        public ImageView mImage;
        private ItemClickListener itemClickListener;

        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.task_name);
            mImage = (ImageView) v.findViewById(R.id.task_icon);
            v.setOnClickListener(this);
        }


        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainMenuAdapter(List<MainMenuTaskInfo> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_display_in_main_menu, parent, false);
        //v.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels / 2;

        MainMenuAdapter.ViewHolder vh = new MainMenuAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MainMenuAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(mDataset.get(position).mTaskName);
        holder.mImage.setImageResource(mDataset.get(position).mImageId);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                FragmentTransaction fragmentTransaction = AdminActivity.mFragmentManager.beginTransaction();
                switch (position) {
                    case 0:
                        fragmentTransaction.replace(R.id.main_view, AdminActivity.mBookListFragment)
                                .addToBackStack("Book list fragment").commit();
                        break;
                    case 1:
                        fragmentTransaction.replace(R.id.main_view, AdminActivity.mUserListFragment)
                                .addToBackStack("User list fragment").commit();
                        break;
                    case 2:
                        fragmentTransaction.replace(R.id.main_view, AdminActivity.mNotificationListFragment)
                                .addToBackStack("Notification list fragment").commit();
                        break;
                    case 3:
                        fragmentTransaction.replace(R.id.main_view, AdminActivity.mReportFragment)
                                .addToBackStack("Report fragment").commit();
                        break;
                    case 4:
                        fragmentTransaction.replace(R.id.main_view, AdminActivity.mBorrowerListFragment)
                                .addToBackStack("Borrower list fragment").commit();
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
