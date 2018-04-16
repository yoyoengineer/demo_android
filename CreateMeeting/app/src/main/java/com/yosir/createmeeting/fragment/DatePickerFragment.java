package com.yosir.createmeeting.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;


import com.yosir.createmeeting.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by yosir on 2018/3/29.
 */

public class DatePickerFragment extends DialogFragment {
//    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.example.yosir.conference.date";//???????????????????????????????????????????????????
    private DatePicker mDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);


        /**
         * retrieving the view of the date picker and set the date sent by the lunching fragment
         */
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title)
                .setView(v);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();

                Date date = new GregorianCalendar(year, month,day).getTime();
                sendResult(Activity.RESULT_OK, date);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

//        builder.create();//????????????????????????????????????????

        return builder.create();








        }

//    public static DatePickerFragment newInstance(Date date){
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_DATE, date);
//
//        DatePickerFragment pickerFragment = new DatePickerFragment();
//        pickerFragment.setArguments(args);
//
//        return pickerFragment;
//    }

    public static DatePickerFragment newInstance(){
        DatePickerFragment pickerFragment = new DatePickerFragment();
        return pickerFragment;
    }

    private void sendResult(int resultCode, Date date){

        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
