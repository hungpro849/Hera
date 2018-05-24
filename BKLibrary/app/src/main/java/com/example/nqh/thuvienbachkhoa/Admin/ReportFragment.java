package com.example.nqh.thuvienbachkhoa.Admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nqh.thuvienbachkhoa.Interface.CallAPI;
import com.example.nqh.thuvienbachkhoa.Model.Book;
import com.example.nqh.thuvienbachkhoa.Model.User;
import com.example.nqh.thuvienbachkhoa.R;
import com.example.nqh.thuvienbachkhoa.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ReportFragment extends Fragment{
    private static final int REQUEST_PERMISSIONS_CODE_WRITE_STORAGE = 123;
    Button startDateBtn, endDateBtn,createReportBtn;
    TextView startDateTextView, endDateTextView;
    Spinner mReportTypeSpinner;
    android.support.v7.widget.Toolbar mToolbar;
    DatePickerDialog.OnDateSetListener mStartDateListener;
    DatePickerDialog.OnDateSetListener mEndDateListener;

    private Activity mCurrentActivity;

    public void setCurrentActivity(Activity activity) {
        this.mCurrentActivity = activity;
    }

    CallAPI getBooks;
    CallAPI getUsers;
    Gson gson;
    SharedPreferences mPrefs;
    String token;

    public ReportFragment() {}
    public ReportFragment getInstance() {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        startDateBtn = view.findViewById(R.id.report_start_date_btn);
        endDateBtn = view.findViewById(R.id.report_end_date_btn);
        createReportBtn=view.findViewById(R.id.create_report);
        startDateTextView = view.findViewById(R.id.report_start_date_text);
        endDateTextView = view.findViewById(R.id.report_end_date_text);
        mToolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.report_tool_bar);
        mReportTypeSpinner = view.findViewById(R.id.report_type_spinner);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getBooks = retrofit.create(CallAPI.class);
        getUsers = retrofit.create(CallAPI.class);
        gson = new Gson();
        mPrefs = this.getActivity().getSharedPreferences("mPrefs",MODE_PRIVATE);
        token = mPrefs.getString("UserToken", null);

        setUpDatePickerButtons();
        setUpReportTypeSpinner();
        setupToolbar();

        return view;
    }

    public void setUpDatePickerButtons() {
        /*
         * setting up listeners
         */

        mStartDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDateTextView.setText(String.format("%d/%d/%d",dayOfMonth,month + 1,year));
            }
        };

        mEndDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDateTextView.setText(String.format("%d/%d/%d",dayOfMonth,month + 1,year));
            }
        };


        /*
         * setting up buttons
         */
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int year = Calendar.YEAR;
                int month = Calendar.MONTH;
                int day = Calendar.DAY_OF_MONTH;
                DatePickerDialog datePickerDialog = new DatePickerDialog(mCurrentActivity,
                        mStartDateListener,
                        year,month,day);
                datePickerDialog.show();
            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int year = Calendar.YEAR;
                int month = Calendar.MONTH;
                int day = Calendar.DAY_OF_MONTH;
                DatePickerDialog datePickerDialog = new DatePickerDialog(mCurrentActivity,
                        mEndDateListener,
                        year,month,day);
                datePickerDialog.show();
            }
        });

        createReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions( //Method of Fragment
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS_CODE_WRITE_STORAGE
                    );
                }
                int pos=mReportTypeSpinner.getSelectedItemPosition();
                switch (pos){
                    case 0:
                    {
                        getBookReport();
                    }
                    case 1:
                    {
                        getUserReport();
                    }
                    case 2:
                    {

                    }
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AdminActivity.mActionBar.hide();
    }

    public void setUpReportTypeSpinner() {
        mReportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Sách");
        categories.add("Người dùng");
        categories.add("Giao dịch");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mCurrentActivity, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mReportTypeSpinner.setAdapter(dataAdapter);
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

    private void getBookReport()
    {
        Call<List<Book>> tokenResponseCall = getBooks.getBooks();
        tokenResponseCall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    // Save user data to SharedPreferences
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    //String userToken = gson.toJson(response.body().getJwt());
                    try{
                        exportBookCSV(response.body());
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
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
            public void onFailure(Call<List<Book>> call, Throwable t) {
                /*mProgress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());*/
                Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void exportBookCSV(final List<Book> response) throws IOException { {
        File folder;

        if(Utils.isExternalStorageWritable()){
            // Get the directory for the app's private pictures directory.
            folder = new File(getContext().getExternalFilesDir("bklibrary"), "Report");
            if (!folder.mkdirs()) {
                Log.e("Report", "Directory not created");
            }
        } else {
            folder = new File(getContext().getFilesDir()
                    + "/Report");
            boolean var = false;
            if (!folder.exists())
                var = folder.mkdir();
        }
        //System.out.println("" + var);


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String date = df.format(Calendar.getInstance().getTime());
        final String filename = folder.toString() + "/" + "BookReport_"+date+".csv";
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0)
                    Toast.makeText(getActivity().getApplicationContext(), "Tạo báo cáo sách thành công tại "+filename, Toast.LENGTH_LONG).show();
            }
        };

        new Thread() {
            public void run() {
                try {

                    Writer fw = new OutputStreamWriter(
                            new FileOutputStream(filename),
                            Charset.forName("UTF-8").newEncoder()
                    );
                    fw.append("Id");
                    fw.append(',');

                    fw.append("Name");
                    fw.append(',');

                    fw.append("Author");
                    fw.append(',');

                    fw.append("Subject");
                    fw.append(',');

                    fw.append("Description");
                    fw.append(',');

                    fw.append("Image_link");
                    fw.append(',');

                    fw.append("Stock");

                    fw.append('\n');

                    for (Book book_rep : response) {
                        fw.append(csvFormat(book_rep.getId()));
                        fw.append(',');

                        fw.append(csvFormat(book_rep.getName()));
                        fw.append(',');

                        fw.append(csvFormat(book_rep.getAuthor()));
                        fw.append(',');

                        fw.append(csvFormat(book_rep.getSubject()));
                        fw.append(',');

                        fw.append(csvFormat(book_rep.getDescription()));
                        fw.append(',');

                        fw.append(csvFormat(book_rep.getImageLink()));
                        fw.append(',');

                        fw.append(csvFormat(book_rep.getStock().toString()));

                        fw.append('\n');
                    }
                    // fw.flush();
                    fw.close();

                } catch (Exception e) {
                }
                Message message = handler.obtainMessage(0);
                message.sendToTarget();
            }
        }.start();

    }

    }
    private void getUserReport()
    {
        Call<List<User>> tokenResponseCall = getUsers.getUsers("Bearer "+token);
        tokenResponseCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    // Save user data to SharedPreferences
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    //String userToken = gson.toJson(response.body().getJwt());
                    try{
                        exportUserCSV(response.body());
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
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
            public void onFailure(Call<List<User>> call, Throwable t) {
                /*mProgress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());*/
                Toast.makeText(getActivity().getApplicationContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void exportUserCSV(final List<User> response) throws IOException {
        {
            File folder;

            if(Utils.isExternalStorageWritable()){
                // Get the directory for the app's private pictures directory.
                folder = new File(getContext().getExternalFilesDir("bklibrary"), "Report");
                if (!folder.mkdirs()) {
                    Log.e("Report", "Directory not created");
                }
            } else {
                folder = new File(getContext().getFilesDir()
                        + "/Report");
                boolean var = false;
                if (!folder.exists())
                    var = folder.mkdir();
            }

            //System.out.println("" + var);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
            String date = df.format(Calendar.getInstance().getTime());
            final String filename = folder.toString() + "/" + "UserReport_"+date+".csv";
            @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==0)
                        Toast.makeText(getActivity().getApplicationContext(), "Tạo báo cáo người dùng thành công tại "+filename, Toast.LENGTH_LONG).show();
                }
            };

            new Thread() {
                public void run() {
                    try {

                        Writer fw = new OutputStreamWriter(
                                new FileOutputStream(filename),
                                Charset.forName("UTF-8").newEncoder()
                        );
                        fw.append("Id");
                        fw.append(',');

                        fw.append("Username");
                        fw.append(',');

                        fw.append("Email");
                        fw.append(',');

                        fw.append("Address");
                        fw.append(',');

                        fw.append("Phone");
                        fw.append(',');

                        fw.append("Fullname");
                        fw.append(',');

                        fw.append("Role");

                        fw.append('\n');

                        for (User user_rep : response) {
                            fw.append(csvFormat(user_rep.getId()));
                            fw.append(',');

                            fw.append(csvFormat(user_rep.getUsername()));
                            fw.append(',');

                            fw.append(csvFormat(user_rep.getEmail()));
                            fw.append(',');

                            fw.append(csvFormat(user_rep.getAddress()));
                            fw.append(',');

                            fw.append(csvFormat(user_rep.getPhone()));
                            fw.append(',');

                            fw.append(csvFormat(user_rep.getFullname()));
                            fw.append(',');

                            fw.append(csvFormat(user_rep.getRole().getType()));

                            fw.append('\n');
                        }
                        // fw.flush();
                        fw.close();

                    } catch (Exception e) {
                    }
                    Message message = handler.obtainMessage(0);
                    message.sendToTarget();
                }
            }.start();

        }

    }
    private String csvFormat(String s)
    {
        s=s.replaceAll("\"","\"\"");
        s="\""+s+"\"";
        return s;
    }

}
