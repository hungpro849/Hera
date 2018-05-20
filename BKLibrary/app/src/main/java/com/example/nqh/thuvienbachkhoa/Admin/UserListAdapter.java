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
import com.example.nqh.thuvienbachkhoa.Model.User;
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

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private List<UserInfoInList> mDataset ;
    private List<User> mUserlist ;
    static public User currentUser;
    private Context context;
    CallAPI delUser;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

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

    public UserListAdapter(Context context) {
        mDataset = Collections.emptyList();
        this.context=context;
    }
    public UserListAdapter(List<UserInfoInList> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_display_in_list, parent, false);

        UserListAdapter.ViewHolder vh = new UserListAdapter.ViewHolder(v);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        delUser = retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = context.getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset.get(position));
        holder.mUsername.setText(mDataset.get(position).mUsername);
        holder.mEmail.setText(mDataset.get(position).mUserEmail);

        /*try {
            User user = database.getById(GeneralUser.class, mDataset.get(position).mUserId);
            if (user.isIslocked()) {
                holder.mLockButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.lock_icon,0,0,0);
            } else {
                holder.mLockButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.lock_open_icon,0,0,0);
            }
        } catch (Exception e){
        }*/
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String uId=mDataset.get(position).mUserId;
                    Call<User> tokenResponseCall = delUser.delUser("Bearer " + token,uId);
                    tokenResponseCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            //mProgress.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Xoá user thành công", Toast.LENGTH_SHORT).show();
                                for (User u:mUserlist)
                                {
                                    if(u.getId().equals(uId))
                                    {
                                        mUserlist.remove(u);
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
                        public void onFailure(Call<User> call, Throwable t) {
                            //mProgress.dismiss();
                            Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    }
            }
        });
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity currentActivity = (AdminActivity) AdminActivity.mUserListFragment.getActivity();
                currentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_view
                        , AdminActivity.mEditUserFragment).addToBackStack("Edit user fragment").commit();
                currentUser = getUserInfo(mDataset.get(position));
                currentActivity.setSupportActionBar(AdminActivity.mEditUserFragment.mToolbar);
            }
        });
        /*holder.mLockButton.setOnClickListener(new View.OnClickListener() {
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
        });*/

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public User getUserInfo(UserInfoInList user) {
        try {
            for(User u : mUserlist)
            {
                if(user.mUserId.equals(u.getId()))
                    return u;
            }

        } catch (Exception e) {
            Log.e("Get User Exception", e.getMessage());
        }
        return mUserlist.get(0);

    }
    public void set_mDataset(List<UserInfoInList> mDataset) {
        this.mDataset = mDataset;
    }
    public void set_mBooklist(List<User> mUserlist) {
        this.mUserlist = mUserlist;
    }
}
