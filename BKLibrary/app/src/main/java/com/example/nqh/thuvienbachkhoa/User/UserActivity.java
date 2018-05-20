package com.example.nqh.thuvienbachkhoa.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nqh.thuvienbachkhoa.DangNhapActivity;
import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.Book;
import com.example.nqh.thuvienbachkhoa.Model.User;
import com.example.nqh.thuvienbachkhoa.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserActivity extends AppCompatActivity {
    String email;
    private List<BookInfoView> mDataset = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BooksAdapter mBooksAdapter;
    CallAPI getBooks;


    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    List<Book> mBookList = new ArrayList<>();
    ActionBar mActionBar;
    Toolbar mToolBar;
    TextView mCurrentUsername;
    Gson gson;
    View headerView;

    SharedPreferences mPrefs;


    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_main);

        setUpDrawer();
        setUpNavigationView();

        // Check for illegal entrance
        mPrefs = getSharedPreferences("mPrefs",MODE_PRIVATE);
        String token = mPrefs.getString("UserToken", null);
        String userData = mPrefs.getString("UserData", null);
        if(token == null || userData == null) {
            Toast.makeText(this, "Vui lòng đăng nhập trước khi sử dụng ứng dụng", Toast.LENGTH_LONG).show();
            finish();
        }

        gson = new Gson();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getBooks = retrofit.create(CallAPI.class);
        //mBooksAdapter = new BooksAdapter(mDataset);

        User user = gson.fromJson(userData, User.class);
        mCurrentUsername.setText(user.getUsername());
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        setEmail(getCurrentUserEmail());
        setUpRecyclerView();


    }

    public  void setUpRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BookInfoView book = mDataset.get(position);
                TextView nameBook=(TextView) view.findViewById(R.id.main_name_book);
                Intent bookInfoIntent = new Intent(getApplicationContext(), getBookInFoActivity.class);
                bookInfoIntent.putExtra("nameBook", nameBook.getText());
                bookInfoIntent.putExtra("email",email);
                getApplicationContext().startActivity(bookInfoIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        loadData();
    }
    public void loadData() {
        Call<List<Book>> tokenResponseCall = getBooks.getBooks();

        tokenResponseCall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                //mProgress.dismiss();
                if (response.isSuccessful()) {
                    // Save user data to SharedPreferences
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    //String userToken = gson.toJson(response.body().getJwt());
                    String books = gson.toJson(response.body());
                    prefsEditor.putString("AllBooks", books);
                    prefsEditor.apply();
                    loadbooks(response.body());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String pastBooks = mPrefs.getString("AllBooks", null);
                        if(pastBooks != null) {
                            List<Book> books = gson.fromJson(pastBooks, new TypeToken<List<Book>>() {
                            }.getType());
                            loadbooks(books);
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                /*mProgress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());*/
                Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                String pastBooks = mPrefs.getString("AllBooks", null);
                if(pastBooks != null) {
                    List<Book> books = gson.fromJson(pastBooks, new TypeToken<List<Book>>() {
                    }.getType());
                    loadbooks(books);
                }
            }
        });
    }

    public void loadbooks(List<Book> response) {
        for (Book book_rep : response) {
            mBookList.add(book_rep);
            //String userData = gson.toJson(book_rep.getBook());
        }
        for (Book b : mBookList) {
            BookInfoView newBook = new BookInfoView(b.getName(), b.getImageLink(), b.getId(), b.getAuthor(), b.getSubject() +"");
            mDataset.add(newBook);
        }
        mBooksAdapter = new BooksAdapter(getApplicationContext(),mDataset);
        mRecyclerView.setAdapter(mBooksAdapter);
    }

    private void setUpDrawer() {
        mDrawerLayout = findViewById(R.id.user_drawer_layout);
        mNavigationView = findViewById(R.id.user_nav_view);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tappe
                        mDrawerLayout.closeDrawer(Gravity.START);
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        // Setup Textview inside navbar
        headerView = mNavigationView.getHeaderView(0);
        mCurrentUsername = headerView.findViewById(R.id.currentUsername);

        mToolBar = findViewById(R.id.user_drawer_toolbar);
        setSupportActionBar(mToolBar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.drawer_menu_icon_white);
        mActionBar.setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user,menu);
        MenuItem myActionMenuItem =menu.findItem(R.id.action_search_user);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
//                    mBooksAdapter.getFilter().filter(s.toString());
//                    mBooksAdapter.getFilter().filter("");
                    //mRecyclerView.clearTextFilter();
                } else {
//                    mBooksAdapter.getFilter().filter(s.toString());
//                    mRecyclerView.setAdapter(mBooksAdapter);

                }
                return true;
            }
        });

        myActionMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mToolBar.setBackgroundColor(Color.WHITE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mToolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
                return true;
            }
        });
        return true;
    }

    public void setUpNavigationView() {
        // Setup actions with navbar
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_drawer_info:
                        Intent userInfo = new Intent(UserActivity.this,UserInfoActivity.class);
                        startActivity(userInfo);
                        break;
                    case R.id.user_drawer_cart:
                        Intent bookBorrow = new Intent(UserActivity.this,BookBorrowActivity.class);
                        startActivity(bookBorrow);
                        break;
                    case R.id.user_drawer_return_book:
                        Intent returnBook = new Intent(UserActivity.this,BookReturnActivity.class);
                        startActivity(returnBook);
                        break;
                    case R.id.user_drawer_borrow_history:
                        Intent borrowHistory = new Intent(UserActivity.this,BorrowBookHistoryActivity.class);
                        startActivity(borrowHistory);
                        break;
                    case R.id.drawer_update:
                        break;
                    case R.id.drawer_settings:
                        break;
                    case R.id.user_drawer_signout:
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.remove("UserToken");
                        editor.remove("UserData");
                        editor.commit();
                        Intent signout = new Intent(UserActivity.this, DangNhapActivity.class);
                        startActivity(signout);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentUserEmail() {
        SharedPreferences session;
        session = this.getSharedPreferences("sessionUser", Context.MODE_PRIVATE);
        String currentEmail = session.getString("email", "");
        return currentEmail;
    }
}
