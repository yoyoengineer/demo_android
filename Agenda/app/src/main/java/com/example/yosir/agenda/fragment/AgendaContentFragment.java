package com.example.yosir.agenda.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yosir.agenda.AgendaContentState;
import com.example.yosir.agenda.R;
import com.example.yosir.agenda.model.Agenda;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.yosir.agenda.AgendaAddActivity.TAG;

public class AgendaContentFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Calendar mCalendar;
    private EditText agendaTitleText;
    private EditText agendaContentText;
    private TextView agendaStartTimeText;
    private TextView agendaEndTimeText;
    private TextView agendaDateText;
    private EditText agendaLocationText;
    private Agenda globalAgenda;

    private static final String DIALOG_AGENDA_DATE = "DialogAgendaDate";
    private static final String DIALOG_AGENDA_START_TIME = "DialogAgendaStartTime";
    private static final String DIALOG_AGENDA_END_TIME = "DialogAgendaEndTime";
    private AgendaContentState agendaContentState = AgendaContentState.BEFOREEDIT;
    private static final int REQUEST_AGENDA_DATE = 0;
    private static final int REQUEST_AGENDA_START_TIME = 1;
    private static final int REQUEST_AGENDA_END_TIME = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.agenda_content_frag, container, false);
        mCalendar = Calendar.getInstance();

        return view;
    }

    public void refresh(Agenda agenda) {
        globalAgenda = agenda;
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        agendaTitleText = (EditText) view.findViewById (R.id.agenda_title);
        agendaContentText = (EditText) view.findViewById(R.id.agenda_content);
        agendaStartTimeText = (TextView) view.findViewById(R.id.agenda_start_time_content);
        agendaEndTimeText = (TextView) view.findViewById(R.id.agenda_end_time_content);
        agendaDateText = (TextView) view.findViewById(R.id.agenda_date_content);
        agendaLocationText = (EditText) view.findViewById(R.id.agenda_location_content);
        agendaTitleText.setText(agenda.getTitle()); // 刷新日程的标题
        agendaContentText.setText(agenda.getContent()); // 刷新日程的内容
        agendaLocationText.setText(agenda.getLocation());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MM dd, yyyy.");
        mCalendar.setTime(agenda.getStartTime());
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = mCalendar.get(Calendar.MINUTE);
        String startTime = ""+hour + ":" + minutes+"";
        mCalendar.setTime(agenda.getEndTime());
        int endHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int endMinutes = mCalendar.get(Calendar.MINUTE);
        String endTime = ""+endHour + ":" + endMinutes+"";
        agendaDateText.setText(simpleDateFormat.format(agenda.getDate()));
        agendaStartTimeText.setText(startTime);
        agendaEndTimeText.setText(endTime);
        agendaDateText.setOnClickListener(this);
        agendaStartTimeText.setOnClickListener(this);
        agendaEndTimeText.setOnClickListener(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        //handle the date picker fragment
        if (requestCode == REQUEST_AGENDA_DATE){
            if (data == null){
                return;
            }
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);


            globalAgenda.setDate(date);
            Log.d(TAG, "onActivityResult: " + globalAgenda.getDate());
            updateDate();
        }

        if (requestCode == REQUEST_AGENDA_START_TIME){
            if (data == null){
                return;
            }
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_DATE_TIME);
            globalAgenda.setStartTime(date);
            updateStartTime();
        }

        if (requestCode == REQUEST_AGENDA_END_TIME){
            if (data == null){
                return;
            }
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_DATE_TIME);
            globalAgenda.setEndTime(date);
            updateEndTime();
        }
    }

    private void updateDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MM dd, yyyy.");
        Log.d(TAG, "updateDate: " + simpleDateFormat.format(globalAgenda.getDate()));
        agendaDateText.setText(simpleDateFormat.format(globalAgenda.getDate()));
    }

    private void updateStartTime(){
        mCalendar.setTime(globalAgenda.getStartTime());
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = mCalendar.get(Calendar.MINUTE);
        String time = ""+hour + ":" + minutes+"";
        agendaStartTimeText.setText(time);
    }

    private void updateEndTime(){
        mCalendar.setTime(globalAgenda.getEndTime());
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = mCalendar.get(Calendar.MINUTE);
        String time = ""+hour + ":" + minutes+"";
        agendaEndTimeText.setText(time);
    }

    public void setEditable(boolean isEditable){
        if (isEditable){
            agendaContentState = AgendaContentState.EDITING;
        }else {
            agendaContentState = AgendaContentState.BEFOREEDIT;
        }
        agendaTitleText.setFocusableInTouchMode(isEditable);
        agendaTitleText.setFocusable(isEditable);
        agendaContentText.setFocusableInTouchMode(isEditable);
        agendaContentText.setFocusable(isEditable);
        agendaLocationText.setFocusableInTouchMode(isEditable);
        agendaLocationText.setFocusable(isEditable);
    }

    public long saveAgenda(){
        long agendaEffected = 0;
        globalAgenda.setLocation(agendaLocationText.getText().toString());
        globalAgenda.setTitle(agendaTitleText.getText().toString());
        globalAgenda.setContent(agendaContentText.getText().toString());

        Agenda agendaToBeUpdated = Agenda.findById(Agenda.class,globalAgenda.getId());
        agendaToBeUpdated.setLocation(globalAgenda.getLocation());
        agendaToBeUpdated.setTitle(globalAgenda.getTitle());
        agendaToBeUpdated.setContent(globalAgenda.getContent());
        agendaToBeUpdated.setStartTime(globalAgenda.getStartTime());
        agendaToBeUpdated.setEndTime(globalAgenda.getEndTime());
        agendaToBeUpdated.setDate(globalAgenda.getDate());

        agendaEffected = agendaToBeUpdated.save();
        return agendaEffected;
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager = getFragmentManager();
        switch (view.getId()){
            case R.id.agenda_date_content:
                if (agendaContentState == AgendaContentState.EDITING){
                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                    datePickerFragment.setTargetFragment(AgendaContentFragment.this, REQUEST_AGENDA_DATE);
                    datePickerFragment.show(manager,DIALOG_AGENDA_DATE);
                }
                break;
            case R.id.agenda_start_time_content:
                if (agendaContentState == AgendaContentState.EDITING){
                    TimePickerFragment dialog = TimePickerFragment.newInstance();
                    dialog.setTargetFragment(AgendaContentFragment.this, REQUEST_AGENDA_START_TIME);
                    dialog.show(manager,DIALOG_AGENDA_START_TIME);
                }
                break;
            case R.id.agenda_end_time_content:
                if (agendaContentState == AgendaContentState.EDITING){
                    TimePickerFragment dialog1 = TimePickerFragment.newInstance();
                    dialog1.setTargetFragment(AgendaContentFragment.this, REQUEST_AGENDA_END_TIME);
                    dialog1.show(manager,DIALOG_AGENDA_END_TIME);
                }
                break;
            default:
                break;
        }
    }
}