package com.example.nqh.thuvienbachkhoa.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class EditBookFragment extends Fragment {
    public Toolbar mToolbar;
    EditText mBookName;
    EditText mBookAuthor;
    EditText mBookSubject;
    EditText mBookDescription;
    EditText mBookLink;
    EditText mBookRemain;
    int mRemain;

    CallAPI editBook;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_book, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.edit_book_tool_bar);

        mBookName = (EditText) view.findViewById(R.id.edit_book_name);
        mBookAuthor = (EditText) view.findViewById(R.id.edit_book_author);
        mBookSubject = (EditText) view.findViewById(R.id.edit_book_subject);
        mBookDescription = (EditText) view.findViewById(R.id.edit_book_description);
        mBookLink = (EditText) view.findViewById(R.id.edit_book_link);
        mBookRemain = (EditText) view.findViewById(R.id.edit_book_remain);


        mToolbar.setTitle(null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        editBook = retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = this.getActivity().getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);
        setupToolbar();
        setupEditButton();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        setupView();
    }

    public void setupView()
    {
        mBookName.setText(BookListAdapter.currentBook.getName());
        mBookAuthor.setText(BookListAdapter.currentBook.getAuthor());
        mBookSubject.setText(BookListAdapter.currentBook.getSubject());
        mBookDescription.setText(BookListAdapter.currentBook.getDescription());
        mBookLink.setText(BookListAdapter.currentBook.getImageLink());
        mBookRemain.setText(Integer.toString(BookListAdapter.currentBook.getStock()));
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
    public void setupEditButton() {
        mToolbar.inflateMenu(R.menu.add_book_menu_item);
        mToolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String bId=BookListAdapter.currentBook.getId();
                String bName = mBookName.getText().toString().trim();
                String bAuthor = mBookAuthor.getText().toString().trim();
                String bSubject = mBookSubject.getText().toString().trim();
                String bDescription = mBookDescription.getText().toString().trim();
                String bLink=mBookLink.getText().toString().trim();
                String bRemain = mBookRemain.getText().toString().trim();

                if (!checkEditBookCondition(bName,bAuthor,bSubject,bDescription,bLink,bRemain)) {
                    try {
                        Call<Book> tokenResponseCall = editBook.editBook("Bearer " + token,bId,bName,bAuthor,bSubject,bDescription,bLink,bRemain);
                        tokenResponseCall.enqueue(new Callback<Book>() {
                            @Override
                            public void onResponse(Call<Book> call, Response<Book> response) {
                                //mProgress.dismiss();
                                if (response.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Chỉnh sửa sách thành công", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();

                                } else {
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                        Toast.makeText(getActivity().getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Book> call, Throwable t) {
                                //mProgress.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Chỉnh sửa sách thất bại", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Xin hãy nhập đầy đủ vào các trường", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public boolean checkEditBookCondition(String bName, String bAuthor, String bSubject, String bDescription, String bLink,String bRemain) {
        //return true if strings are empty or null
        boolean conditionName = TextUtils.isEmpty(bName);
        boolean conditionAuthor = TextUtils.isEmpty(bAuthor);
        boolean conditionSubject = TextUtils.isEmpty(bSubject);
        boolean conditionDescription = TextUtils.isEmpty(bDescription);
        boolean conditionLink = TextUtils.isEmpty(bLink);
        boolean conditionRemain = TextUtils.isEmpty(bRemain);
        return conditionName || conditionAuthor || conditionSubject || conditionDescription ||conditionLink|| conditionRemain;
    }


}
