package com.example.yosir.agenda.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.yosir.agenda.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yosir on 2018/3/30.
 */

public class TimePickerFragment extends DialogFragment {
//    private static final String ARGS_DATE_TIME = "date_time";
    public static final String EXTRA_DATE_TIME = "extra_date_time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(){
        TimePickerFragment pickerDialog = new TimePickerFragment();
        return pickerDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar mCalendar = Calendar.getInstance();
        //set the actual Time
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = mCalendar.get(Calendar.MINUTE);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = view.findViewById(R.id.time_picker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minutes);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minutes);
        }


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour, minutes;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            hour = mTimePicker.getHour();
                            minutes = mTimePicker.getMinute();
                        } else {
                            hour = mTimePicker.getCurrentHour();
                            minutes = mTimePicker.getCurrentMinute();
                        }
                        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        mCalendar.set(Calendar.MINUTE, minutes);
                        sendResult(Activity.RESULT_OK, mCalendar.getTime());
                    }
                })
                .create();
    }


    private void sendResult(int resultCode, Date date){

        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
