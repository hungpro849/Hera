package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.Book;
import com.example.nqh.thuvienbachkhoa.R;
import com.example.nqh.thuvienbachkhoa.Utils;
import com.example.nqh.thuvienbachkhoa.doipasswordActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddBookFragment extends Fragment {
    Toolbar mToolbar;
    EditText mBookName;
    EditText mAuthor;
    EditText mSubject;
    EditText mDescription;
    EditText mRemain;
    EditText mImageLink;
    ProgressDialog mProgress;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);
        mToolbar = view.findViewById(R.id.add_book_tool_bar);

        mBookName = view.findViewById(R.id.add_book_name);
        mAuthor = view.findViewById(R.id.add_book_author);
        mSubject = view.findViewById(R.id.add_book_subject);
        mDescription = view.findViewById(R.id.add_book_description);
        mRemain = view.findViewById(R.id.add_book_remain);
        mImageLink = view.findViewById(R.id.add_book_image);

        mToolbar.setTitle(null);
        setupToolbar();
        setupAcceptButton();
        return view;
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
    public void setupAcceptButton() {
        mToolbar.inflateMenu(R.menu.add_book_menu_item);
        mToolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String bName = mBookName.getText().toString().trim();
                String bAuthor = mAuthor.getText().toString().trim();
                String bSubject = mSubject.getText().toString().trim();
                String bDescription = mDescription.getText().toString().trim();
                int bRemain = Integer.parseInt(mRemain.getText().toString());
                String bImage = mImageLink.getText().toString().trim();

                if (!checkAddBookCondition(bName,bAuthor,bSubject,bDescription)) {
                    // Setup progress diaglog view
                    mProgress = new ProgressDialog(getActivity());
                    mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgress.setMessage(getString(R.string.waiting_message));
                    mProgress.setIndeterminate(true);
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    SharedPreferences mPrefs = getActivity().getSharedPreferences("mPrefs", Context.MODE_PRIVATE);
                    String token = mPrefs.getString("UserToken", null);
                    if(token != null) {
                        // Retrofit
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(getString(R.string.api_url))
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        CallAPI service = retrofit.create(CallAPI.class);
                        Call<Book> bookCall = service.createBook("Bearer " + token, bName, bAuthor, bSubject, bDescription,
                                bImage, bRemain);
                        bookCall.enqueue(new Callback<Book>() {
                            @Override
                            public void onResponse(Call<Book> call, Response<Book> response) {
                                mProgress.dismiss();
                                if(response.isSuccessful()) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                    builder1.setMessage("Bạn đã thêm sách mới thành công");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "Đồng ý",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    Utils.hideKeyboard(getActivity());
                                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                    fragmentManager.beginTransaction()
                                                            .replace(R.id.main_view, AdminActivity.mBookListFragment)
                                                            .commit();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                } else {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Book> call, Throwable t) {
                                mProgress.dismiss();
                                Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }


                } else {
                    Toast.makeText(getActivity(), "Xin hãy nhập đầy đủ vào các trường", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public boolean checkAddBookCondition(String bName, String bAuthor, String bSubject, String bDescription) {
        //return true if strings are empty or null
        boolean conditionName = TextUtils.isEmpty(bName);
        boolean conditionAuthor = TextUtils.isEmpty(bAuthor);
        boolean conditionSubject = TextUtils.isEmpty(bSubject);
        boolean conditionDescription = TextUtils.isEmpty(bDescription);
        return conditionName || conditionAuthor || conditionSubject || conditionDescription;
    }

}

