package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.BookResponse;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class BookListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity mCurrentActivity;
    private BookListAdapter mBookListAdapter;
    private DBHelper database;
    private Toolbar mToolbar;
    ProgressDialog mProgress;
    private SearchView mSearchView;
    private FloatingActionButton mAddBookBtn;
    public List<BookInfoInList> mDataset = new Vector<BookInfoInList>();
    public List<Book> mBookList = new Vector<Book>();
    CallAPI getBooks;
    CallAPI delBook;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

    public void setCurrentActivity(Activity activity) {
        this.mCurrentActivity = activity;
    }


    public void setDatabase(DBHelper db) {
        this.database = db;
    }

    public void setDataset(List<Book> bookList) {
        mBookList = bookList;
        mDataset.removeAll(mDataset);
        for (Book b : mBookList) {
            BookInfoInList newBook = new BookInfoInList(b);
            mDataset.add(newBook);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.book_list_recycle_view);
        mToolbar = (Toolbar) view.findViewById(R.id.book_list_tool_bar);
        mAddBookBtn = (FloatingActionButton) view.findViewById(R.id.add_book_btn);
        mDataset.clear();
        mBookList.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getBooks = retrofit.create(CallAPI.class);
        delBook= retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = this.getActivity().getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);
        setUpRecyclerView();
        setupToolbar();
        setupSearchView();
        setupAddBookEditButton();;
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AdminActivity.mActionBar.hide();
    }

    public void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new BookListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mCurrentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadData();


    }

    public void loadData() {
        Call<List<BookResponse>> tokenResponseCall = getBooks.getBooks();
        mProgress = new ProgressDialog(getActivity()); // this = YourActivity
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage("Getting data ...");
        mProgress.setIndeterminate(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        tokenResponseCall.enqueue(new Callback<List<BookResponse>>() {
            @Override
            public void onResponse(Call<List<BookResponse>> call, Response<List<BookResponse>> response) {
                mProgress.dismiss();
                if (response.isSuccessful()) {
                    // Save user data to SharedPreferences
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    //String userToken = gson.toJson(response.body().getJwt());
                    String books = gson.toJson(response.body());
                    prefsEditor.putString("AllBooks", books);
                    prefsEditor.apply();
                    loadbooks(response.body());
                    ((BookListAdapter)mAdapter).set_mDataset(mDataset);
                    ((BookListAdapter)mAdapter).set_mBooklist(mBookList);
                    mAdapter.notifyDataSetChanged();

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getActivity().getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String pastBooks = mPrefs.getString("AllBooks", null);
                        if(!pastBooks.equals(null)) {
                            List<BookResponse> books = gson.fromJson(pastBooks, new TypeToken<List<BookResponse>>() {
                            }.getType());
                            loadbooks(books);
                        }
                        ((BookListAdapter)mAdapter).set_mDataset(mDataset);
                        ((BookListAdapter)mAdapter).set_mBooklist(mBookList);
                        mAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<BookResponse>> call, Throwable t) {
                mProgress.dismiss();
                /*mProgress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());*/
                Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                String pastBooks = mPrefs.getString("AllBooks", null);
                if(pastBooks!=null) {
                    List<BookResponse> books = gson.fromJson(pastBooks, new TypeToken<List<BookResponse>>() {
                    }.getType());
                    loadbooks(books);
                }
                ((BookListAdapter)mAdapter).set_mDataset(mDataset);
                ((BookListAdapter)mAdapter).set_mBooklist(mBookList);
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    public void loadbooks(List<BookResponse> response)
    {
        for (BookResponse book_rep : response) {
            mBookList.add(book_rep.getBook());
        }
        for (Book b : mBookList) {
            BookInfoInList newBook = new BookInfoInList(b);
            mDataset.add(newBook);
        }

    }
    public void setupToolbar() {
        mToolbar.setNavigationIcon(R.drawable.back_button_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void setupSearchView() {
        mToolbar.inflateMenu(R.menu.material_search_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        mSearchView = (android.support.v7.widget.SearchView) mToolbar.getMenu().getItem(0).getActionView();
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                loadDataAfterQuery(bookQuery(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                {
                    mDataset.clear();
                    for (Book b: mBookList) {
                        BookInfoInList newBook = new BookInfoInList(b);
                        mDataset.add(newBook);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        mToolbar.getMenu().getItem(0).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mToolbar.setBackgroundColor(Color.WHITE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
                return true;
            }
        });
    }

    public List<Book> bookQuery(String query) {
        List<Book> foundBook = new Vector<Book>();

        String patt = "\\b"+query+".*?\\b";
        Pattern regex = Pattern.compile(patt,Pattern.CASE_INSENSITIVE);
        for (Book b : mBookList) {
            if(regex.matcher(b.getName()).find()){
                foundBook.add(b);
            }

        }
        return foundBook;
    }

    public void loadDataAfterQuery(List<Book> bookList) {
        mDataset.clear();
        for (Book b: bookList) {
            BookInfoInList newBook = new BookInfoInList(b);
            mDataset.add(newBook);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setupAddBookEditButton() {
        mAddBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_view, AdminActivity.mAddBookFragment)
                        .addToBackStack("Add book fragment").commit();
            }
        });
    }
    public void delBook()
    {

    }
}
