package com.example.nqh.thuvienbachkhoa.Admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.nqh.thuvienbachkhoa.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportFragment extends Fragment{
    Button startDateBtn, endDateBtn;
    TextView startDateTextView, endDateTextView;
    Spinner mReportTypeSpinner;
    android.support.v7.widget.Toolbar mToolbar;
    DatePickerDialog.OnDateSetListener mStartDateListener;
    DatePickerDialog.OnDateSetListener mEndDateListener;

    private Activity mCurrentActivity;

    public void setCurrentActivity(Activity activity) {
        this.mCurrentActivity = activity;
    }

    boolean startBtnChose = false;

    public ReportFragment() {}
    public ReportFragment getInstance() {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        startDateBtn = view.findViewById(R.id.report_start_date_btn);
        endDateBtn = view.findViewById(R.id.report_end_date_btn);

        startDateTextView = view.findViewById(R.id.report_start_date_text);
        endDateTextView = view.findViewById(R.id.report_end_date_text);
        mToolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.report_tool_bar);
        mReportTypeSpinner = view.findViewById(R.id.report_type_spinner);

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
        categories.add("Lưu lượng");
        categories.add("Hàng hóa");
        categories.add("Người dùng");
        categories.add("Hoạt động");

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

}
