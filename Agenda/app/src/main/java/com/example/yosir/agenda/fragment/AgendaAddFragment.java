package com.example.yosir.agenda.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yosir.agenda.R;
import com.example.yosir.agenda.model.Agenda;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgendaAddFragment extends Fragment implements View.OnClickListener{

    private static final String DIALOG_AGENDA_DATE = "DialogAgendaDate";
    private static final String DIALOG_AGENDA_START_TIME = "DialogAgendaStartTime";
    private static final String DIALOG_AGENDA_END_TIME = "DialogAgendaEndTime";
    private static final int REQUEST_AGENDA_DATE = 0;
    private static final int REQUEST_AGENDA_START_TIME = 1;
    private static final int REQUEST_AGENDA_END_TIME = 2;
    private View view;
    private TextView date;
    private TextView agendaStartTime;
    private TextView agendaEndTime;
    private EditText agendaTitle;
    private EditText agendaContent;
    private EditText agendaLocation;
    private Calendar mCalendar;

    private Agenda agenda = new Agenda();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_agenda_add, container, false);
        date = view.findViewById(R.id.agenda_date);
        mCalendar = Calendar.getInstance();
        agendaTitle = view.findViewById(R.id.agenda_title);
        agendaContent = view.findViewById(R.id.agenda_content);
        agendaStartTime = view.findViewById(R.id.agenda_start_time);
        agendaEndTime = view.findViewById(R.id.agenda_end_time);
        agendaLocation = view.findViewById(R.id.agenda_location);
        date.setOnClickListener(this);
        agendaStartTime.setOnClickListener(this);
        agendaEndTime.setOnClickListener(this);
        return view;
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


            agenda.setDate(date);
            updateDate();
        }


        //handle the time picker fragment
        if (requestCode == REQUEST_AGENDA_START_TIME){
            if (data == null){
                return;
            }
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_DATE_TIME);
            agenda.setStartTime(date);
            updateStartTime();
        }

        if (requestCode == REQUEST_AGENDA_END_TIME){
            if (data == null){
                return;
            }
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_DATE_TIME);
            agenda.setEndTime(date);
            updateEndTime();
        }
    }

    public void refresh() {
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
    }

    private void updateDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MM dd, yyyy.");
        date.setText(simpleDateFormat.format(agenda.getDate()));
    }

    private void updateStartTime(){
        mCalendar.setTime(agenda.getStartTime());
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = mCalendar.get(Calendar.MINUTE);
        String time = ""+hour + ":" + minutes+"";
        agendaStartTime.setText(time);
    }

    private void updateEndTime(){
        mCalendar.setTime(agenda.getEndTime());
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = mCalendar.get(Calendar.MINUTE);
        String time = ""+hour + ":" + minutes+"";
        agendaEndTime.setText(time);
    }

    public long saveAgenda() {
        long agendaAffected = 0;
        agenda.setLocation(agendaLocation.getText().toString());
        agenda.setTitle(agendaTitle.getText().toString());
        agenda.setContent(agendaContent.getText().toString());
        if (agenda.getContent() == null || agenda.getContent().isEmpty() || agenda.getDate() == null || agenda.getLocation() == null || agenda.getLocation().isEmpty() || agenda.getTitle() == null || agenda.getTitle().isEmpty() || agenda.getStartTime() == null || agenda.getEndTime() == null){
            Toast.makeText(getActivity(),"Please enter all the information.",Toast.LENGTH_SHORT).show();
        }else {
            agendaAffected = agenda.save();
        }
        return agendaAffected;
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager = getFragmentManager();
         switch (view.getId()){
             case R.id.agenda_date:
                 DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                 datePickerFragment.setTargetFragment(AgendaAddFragment.this, REQUEST_AGENDA_DATE);
                 datePickerFragment.show(manager,DIALOG_AGENDA_DATE);
                 break;
             case R.id.agenda_start_time:
                 TimePickerFragment dialog = TimePickerFragment.newInstance();
                 dialog.setTargetFragment(AgendaAddFragment.this, REQUEST_AGENDA_START_TIME);
                 dialog.show(manager,DIALOG_AGENDA_START_TIME);
                 break;
             case R.id.agenda_end_time:
                 TimePickerFragment dialog1 = TimePickerFragment.newInstance();
                 dialog1.setTargetFragment(AgendaAddFragment.this, REQUEST_AGENDA_END_TIME);
                 dialog1.show(manager,DIALOG_AGENDA_END_TIME);
                 break;
             default:
                 break;
         }
    }
}
