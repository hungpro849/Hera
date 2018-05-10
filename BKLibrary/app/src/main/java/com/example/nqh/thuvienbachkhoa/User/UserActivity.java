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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import com.example.nqh.thuvienbachkhoa.Admin.AdminActivity;
import com.example.nqh.thuvienbachkhoa.DangNhapActivity;
import com.example.nqh.thuvienbachkhoa.Database.db.DBHelper;
import com.example.nqh.thuvienbachkhoa.Database.models.Book;
import com.example.nqh.thuvienbachkhoa.R;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    String email;
    ListView lv;
    mainUserAdapter adapter;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    ArrayList<line_main_user_infor> arrayList = new ArrayList<line_main_user_infor>();
    DBHelper database;
    ArrayList<Book> mBookList = new ArrayList<Book>();
    ActionBar mActionBar;
    Toolbar mToolBar;


    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_main);

        setUpDrawer();
        setUpNavigationView();

        lv = (ListView) findViewById(R.id.MU_lv);
        database = new DBHelper(this);
        //DBHelper.createDemoData(database);
        loadData();
        setEmail(getCurrentUserEmail());

        //pass rasult adapter to the list view
        adapter = new mainUserAdapter(this, arrayList, email);
        lv.setAdapter(adapter);
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

        mToolBar = findViewById(R.id.user_drawer_toolbar);
        setSupportActionBar(mToolBar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.drawer_menu_icon_white);
        mActionBar.setHomeButtonEnabled(true);
    }


    public void loadData() {
        try {
            mBookList = (ArrayList<Book>) database.getAllOrdered(Book.class, "name", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Book b : mBookList) {
            line_main_user_infor newBook = new line_main_user_infor(b.getName(), R.drawable.bookex);
            arrayList.add(newBook);
        }

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
                    adapter.filter("");
                    lv.clearTextFilter();
                } else {
                    adapter.filter(s);
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
                        Intent signout = new Intent(UserActivity.this, DangNhapActivity.class);
                        startActivity(signout);
                        SharedPreferences session = UserActivity.this.getSharedPreferences("sessionUser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = session.edit();
                        editor.clear();
                        editor.commit();
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
