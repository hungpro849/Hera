package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.DangNhapActivity;
import com.example.nqh.thuvienbachkhoa.Model.User;
import com.example.nqh.thuvienbachkhoa.R;
import com.example.nqh.thuvienbachkhoa.Utils;
import com.google.gson.Gson;

public class AdminActivity extends AppCompatActivity {
    Bitmap download;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    public static Toolbar mToolbar;
    public static ActionBar mActionBar;
    private SearchView mSearchView;
    public static BookListFragment mBookListFragment;
    public static MainMenuFragment mMainMenuFragment;
    public static UserListFragment mUserListFragment;
    public static NotificationListFragment mNotificationListFragment;
    public static OverlayFrame mOverlayFrame;
    public static ReportFragment mReportFragment;
    public static NotificationFragment mNotificationFragment;
    public static AddBookFragment mAddBookFragment;
    public static EditBookFragment mEditBookFragment;
    public static EditUserFragment mEditUserFragment;
    public static BorrowerListFragment mBorrowerListFragment;
    public static BorrowerInfoFragment mBorrowerInfoFragment;
    public static BarcodeFragment mBarcodeFragment;

    public static FragmentManager mFragmentManager;

    View headerView;
    TextView mCurrentUsername;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Main Activity", "On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_main);

        setUpDrawer();
        setUpNavigationView();

        // Check for illegal entrance
        mPrefs = getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);
        String userData = mPrefs.getString("UserData", null);
        if(token == null || userData == null) {
            Toast.makeText(this, "Vui lòng đăng nhập trước khi sử dụng ứng dụng", Toast.LENGTH_LONG).show();
            finish();
        }

        gson = new Gson();

        User user = gson.fromJson(userData, User.class);

        mCurrentUsername.setText(user.getUsername());

        mBookListFragment = new BookListFragment();
        mMainMenuFragment = new MainMenuFragment();
        mUserListFragment = new UserListFragment();
        mEditUserFragment = new EditUserFragment();
        mBarcodeFragment = new BarcodeFragment();

        mReportFragment = new ReportFragment();
        mReportFragment.setCurrentActivity(AdminActivity.this);

        mNotificationFragment = new NotificationFragment();

        mNotificationListFragment = new NotificationListFragment();

        mAddBookFragment = new AddBookFragment();

        mEditBookFragment = new EditBookFragment();

        mBorrowerListFragment = new BorrowerListFragment();

        mBorrowerInfoFragment = new BorrowerInfoFragment();

        mOverlayFrame = findViewById(R.id.overlay);
        mOverlayFrame.setOverlay(false);

        //load book recycle view into main view
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.main_view, mMainMenuFragment).commit();

    }

    private void setUpDrawer() {
        mDrawerLayout = findViewById(R.id.admin_drawer_layout);
        mNavigationView = findViewById(R.id.admin_nav_view);
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

        mToolbar = findViewById(R.id.drawer_toolbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.drawer_menu_icon_white);
        mActionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
//            case R.id.action_search:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUpNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {

                    case R.id.drawer_update:
                        break;
                    case R.id.drawer_settings:
                        break;
                    case R.id.drawer_signout:
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.remove("UserToken");
                        editor.remove("UserData");
                        editor.commit();
                        Intent signout = new Intent(AdminActivity.this, DangNhapActivity.class);
                        startActivity(signout);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        int currentBackStackSize = mFragmentManager.getBackStackEntryCount();
        if (currentBackStackSize > 0) {
            if (currentBackStackSize == 1) mActionBar.show();
            mFragmentManager.popBackStack();
        } else {
            finish();
        }
        // Close all keyboard
        Utils.hideKeyboard(this);
    }

    public String getToken() {
        return token;
    }
}
