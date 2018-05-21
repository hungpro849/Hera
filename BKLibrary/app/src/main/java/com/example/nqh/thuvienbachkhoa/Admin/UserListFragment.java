package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.Book;
import com.example.nqh.thuvienbachkhoa.Model.User;
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

public class UserListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity mCurrentActivity;
    private UserListAdapter mUserListAdapter;
    private Toolbar mToolbar;
    ProgressDialog mProgress;
    private android.support.v7.widget.SearchView mSearchView;
    public void setCurrentActivity(Activity activity) {
        this.mCurrentActivity = activity;
    }
    private List<UserInfoInList> mDataset = new Vector<UserInfoInList>();
    private List<User> mUserList = new Vector<User>();
    CallAPI getUsers;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.user_list_recycle_view);
//        mSearchView = (MaterialSearchView) view.findViewById(R.id.user_search_view);
        mToolbar = (Toolbar) view.findViewById(R.id.user_list_tool_bar);
        mUserList.clear();
        mDataset.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getUsers = retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = this.getActivity().getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);

        setUpRecyclerView();
        setupToolbar();
        setupSearchView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AdminActivity.mActionBar.hide();
    }

    public void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new UserListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mCurrentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadData();


    }
    public void loadData() {
        try {
            Call<List<User>> tokenResponseCall = getUsers.getUsers("Bearer "+token);
            mProgress = new ProgressDialog(getActivity()); // this = YourActivity
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setMessage("Getting data ...");
            mProgress.setIndeterminate(true);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            tokenResponseCall.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    mProgress.dismiss();
                    if (response.isSuccessful()) {
                        // Save user data to SharedPreferences
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        //String userToken = gson.toJson(response.body().getJwt());
                        String users = gson.toJson(response.body());
                        prefsEditor.putString("AllUsers", users);
                        prefsEditor.apply();
                        loadusers(response.body());
                        ((UserListAdapter)mAdapter).set_mDataset(mDataset);
                        ((UserListAdapter)mAdapter).set_mBooklist(mUserList);
                        mAdapter.notifyDataSetChanged();

                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getActivity().getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            String pastUserss = mPrefs.getString("AllUsers", null);
                            if(!pastUserss.equals(null)) {
                                List<User> users = gson.fromJson(pastUserss, new TypeToken<List<User>>() {
                                }.getType());
                                loadusers(users);
                            }
                            ((UserListAdapter)mAdapter).set_mDataset(mDataset);
                            ((UserListAdapter)mAdapter).set_mBooklist(mUserList);
                            mAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    mProgress.dismiss();
                /*mProgress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());*/
                    Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                    String pastUserss = mPrefs.getString("AllUsers", null);
                    if(!pastUserss.equals(null)) {
                        List<User> users = gson.fromJson(pastUserss, new TypeToken<List<User>>() {
                        }.getType());
                        loadusers(users);
                    }
                    ((UserListAdapter)mAdapter).set_mDataset(mDataset);
                    ((UserListAdapter)mAdapter).set_mBooklist(mUserList);
                    mAdapter.notifyDataSetChanged();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadusers(List<User> response)
    {
        for (User user_rep : response) {
            if(!user_rep.isAdmin())
                mUserList.add(user_rep);
        }
        for (User u : mUserList) {
            UserInfoInList newUser = new UserInfoInList(u);
            mDataset.add(newUser);
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
                loadDataAfterQuery(userQuery(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                {
                    mDataset.clear();
                    for (User u: mUserList) {
                        UserInfoInList newUser = new UserInfoInList(u);
                        mDataset.add(newUser);
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

    public List<User> userQuery(String query) {
        List<User> foundUser = new Vector<User>();
        String patt = "\\b"+query+".*?\\b";
        Pattern regex = Pattern.compile(patt,Pattern.CASE_INSENSITIVE);
        for (User u : mUserList) {
            if(regex.matcher(u.getUsername()).find()){
                foundUser.add(u);
            }
        }
        return foundUser;
    }

    public void loadDataAfterQuery(List<User> userList) {
        mDataset.clear();
        for (User u: userList) {
            UserInfoInList newUser= new UserInfoInList(u);
            mDataset.add(newUser);
        }
        mAdapter.notifyDataSetChanged();
    }
}
