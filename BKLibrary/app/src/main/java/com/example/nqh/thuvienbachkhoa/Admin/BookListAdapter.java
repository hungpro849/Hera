package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.Book;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private List<BookInfoInList> mDataset ;
    private List<Book> mBooklist ;
    static public Book currentBook;
    private Context context;
    CallAPI delBook;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mAuthor;
        public TextView mTitle;
        public Button mDeleteButton;
        public Button mEditButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.book_list_title);
            mAuthor = itemView.findViewById(R.id.book_list_author);
            mDeleteButton = itemView.findViewById(R.id.delete_book_button);
            mEditButton = itemView.findViewById(R.id.edit_book_button);

        }
    }

    public BookListAdapter(Context context) {
        mDataset = Collections.emptyList();
        this.context = context;
    }

    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_display_in_list, parent, false);

        BookListAdapter.ViewHolder vh = new BookListAdapter.ViewHolder(v);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        delBook = retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = context.getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);

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
                //database.deleteById(Book.class, mDataset.get(position).mBookId);

                //mDataset.remove(position);
                //notifyDataSetChanged();
                try {
                    final String bId=mDataset.get(position).mBookId;
                    Call<Book> tokenResponseCall = delBook.delBook("Bearer " + token,bId);
                    tokenResponseCall.enqueue(new Callback<Book>() {
                        @Override
                        public void onResponse(Call<Book> call, Response<Book> response) {
                            //mProgress.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Xoá sách thành công", Toast.LENGTH_SHORT).show();
                                for (Book b:mBooklist)
                                {
                                    if(b.getId().equals(bId))
                                    {
                                        mBooklist.remove(b);
                                        break;
                                    }
                                }
                                mDataset.remove(position);
                                notifyDataSetChanged();

                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Book> call, Throwable t) {
                            //mProgress.dismiss();
                            Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(context, "Xoá sách thất bại", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        });

        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity currentActivity = (AdminActivity) AdminActivity.mBookListFragment.getActivity();
                currentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_view
                        , AdminActivity.mEditBookFragment).addToBackStack("Edit book fragment").commit();
                currentBook = getbookInfo(mDataset.get(position));
                currentActivity.setSupportActionBar(AdminActivity.mEditBookFragment.mToolbar);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public Book getbookInfo(BookInfoInList book) {
        try {
            for(Book b : mBooklist)
            {
                if(book.mBookId.equals(b.getId()))
                    return b;
            }

        } catch (Exception e) {
            Log.e("Get Book Exception", e.getMessage());
        }
        return mBooklist.get(0);

    }
    public void set_mDataset(List<BookInfoInList> mDataset) {
        this.mDataset = mDataset;
    }
    public void set_mBooklist(List<Book> mBooklist) {
        this.mBooklist = mBooklist;
    }


}


