package com.example.nqh.thuvienbachkhoa.Admin;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private List<BookInfoInList> mDataset = Collections.emptyList();
    private DBHelper database;
    private Book currentBook;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mAuthor;
        public TextView mTitle;
        public Button mDeleteButton;
        public Button mEditButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.book_list_title);
            mAuthor = (TextView) itemView.findViewById(R.id.book_list_author);
            mDeleteButton = (Button) itemView.findViewById(R.id.delete_book_button);
            mEditButton = (Button) itemView.findViewById(R.id.edit_book_button);
        }
    }

    public BookListAdapter(List<BookInfoInList> myDataset, DBHelper db) {
        mDataset = myDataset;
        database = db;
    }


    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_display_in_list, parent, false);

        BookListAdapter.ViewHolder vh = new BookListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BookListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset.get(position));
        holder.mTitle.setText(mDataset.get(position).mTitle);
        holder.mAuthor.setText(mDataset.get(position).mAuthor);
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    database.deleteById(Book.class, mDataset.get(position).mBookId);
                    mDataset.remove(position);
                    notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        });

        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity currentActivity = (AdminActivity) AdminActivity.mBookListFragment.getActivity();
                currentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_view
                        , AdminActivity.mEditBookFragment).addToBackStack("Edit book fragment").commit();
                currentBook = getbookInfo(mDataset.get(position).mTitle);
                currentActivity.mEditBookFragment.displayInitialData(currentBook.getName(),
                        currentBook.getAuthor(),
                        currentBook.getSubject(),
                        currentBook.getDescription(),
                        currentBook.getRemain());
                currentActivity.setSupportActionBar(AdminActivity.mEditBookFragment.mToolbar);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Book getbookInfo(String bookName) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("name", bookName);
        List<Book> foundBook = Collections.emptyList();
        try {
            foundBook = database.query(Book.class, condition);
        } catch (Exception e) {
            Log.e("BookResponse Query Exception", e.getMessage());
        }
        return foundBook.get(0);
    }

}


