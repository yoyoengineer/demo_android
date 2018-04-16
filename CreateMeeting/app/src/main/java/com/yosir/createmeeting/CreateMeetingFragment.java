package com.yosir.createmeeting;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.yosir.createmeeting.fragment.DatePickerFragment;
import com.yosir.createmeeting.fragment.TimePickerFragment;
import com.yosir.createmeeting.model.Conference;
import com.yosir.createmeeting.model.Location;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class CreateMeetingFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    private Conference conference = new Conference();
    private Location location;
    private static final String DIALOG_MEETING_DATE = "DialogAgendaDate";
    private static final String DIALOG_MEETING_TIME = "DialogAgendaStartTime";
    private static final int REQUEST_MEETING_DATE = 0;
    private static final int REQUEST_MEETING_TIME = 1;
    private static final int REQUEST_MEETING_LOCATION = 2;

    private static final String TAG = "create_meeting";

    String[] listItems;
    private String conferenceDate = "";
    private String conferenceTime = "";
//    List<String> listItems;

    private EditText topic;
    private EditText description;
    private TextView time;
    private TextView address;
    private EditText specificAddress;
    private TextView invitees;
    private TextView otherSpeakers;
    private Switch isPrivate;
    private Switch meSpeaker;
    private Button createMeeting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_meeting, container, false);
        topic =view.findViewById(R.id.topic_createmeeting);
        description = view.findViewById(R.id.description_createmeeting);
        time = view.findViewById(R.id.time_createmeeting);
        address = view.findViewById(R.id.adress_createmeeting);
        specificAddress = view.findViewById(R.id.specificAddress_createmeeting);
        invitees = view.findViewById(R.id.invitees_createmeeting);
        otherSpeakers = view.findViewById(R.id.otherSpeakers_createmeeting);
        isPrivate = view.findViewById(R.id.isPrivate_createmeeting);
        meSpeaker = view.findViewById(R.id.meSpeaker_createmeeting);
        createMeeting = view.findViewById(R.id.createMeeting_createmeeting);

        invitees.setOnClickListener(this);
        otherSpeakers.setOnClickListener(this);
        isPrivate.setOnCheckedChangeListener(this);
        meSpeaker.setOnCheckedChangeListener(this);
        time.setOnClickListener(this);
        address.setOnClickListener(this);
        createMeeting.setOnClickListener(this);
        List<String> friends =getFriends();
        listItems = friends.toArray(new String[friends.size()]);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        //handle the date picker fragment
        if (requestCode == REQUEST_MEETING_DATE){
            if (data == null){
                return;
            }
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            conferenceDate = simpleDateFormat.format(date);
        }


        //handle the time picker fragment
        if (requestCode == REQUEST_MEETING_TIME){
            if (data == null){
                return;
            }
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_DATE_TIME);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            conferenceTime = simpleDateFormat.format(date);
            if (!(conferenceDate.isEmpty()||conferenceTime.isEmpty())){
                time.setText(conferenceDate + " " + conferenceTime);
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    conference.setTime(f.parse(conferenceDate + " " + conferenceTime).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (data != null && requestCode == REQUEST_MEETING_LOCATION){
            Location location = (Location)data.getSerializableExtra("location");
            Log.i(TAG, "onCreate: " + location.getAddress());
            conference.setLocation(location);
            address.setText(location.getAddress());
        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager = getFragmentManager();
        switch (view.getId()){
            case R.id.time_createmeeting:
                TimePickerFragment dialog = TimePickerFragment.newInstance();
                dialog.setTargetFragment(CreateMeetingFragment.this, REQUEST_MEETING_TIME);
                dialog.show(manager,DIALOG_MEETING_TIME);
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                datePickerFragment.setTargetFragment(CreateMeetingFragment.this, REQUEST_MEETING_DATE);
                datePickerFragment.show(manager,DIALOG_MEETING_DATE);
                break;
            case R.id.invitees_createmeeting:
                boolean[] checkedItems;
                checkedItems = new boolean[listItems.length];
                ArrayList<Integer> mUserItems = new ArrayList<>();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Choose Invitees");
                final ArrayList<Integer> finalMUserItems = mUserItems;
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!finalMUserItems.contains(position)) {
                                finalMUserItems.add(position);
                            }
                        } else {
                            if (finalMUserItems.contains(position)) {
                                finalMUserItems.remove(finalMUserItems.indexOf(position));
                            }
                        }
                    }
                });

                mBuilder.setCancelable(false);
                final ArrayList<Integer> finalMUserItems2 = mUserItems;
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        List<String> inviteesInsert = new ArrayList<>();
                        for (int i = 0; i < finalMUserItems2.size(); i++) {
                            item = item + listItems[finalMUserItems2.get(i)];
                            inviteesInsert.add(listItems[finalMUserItems2.get(i)]);
                            if (i != finalMUserItems2.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        invitees.setText(item);
                        conference.setInvitees(inviteesInsert);
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                final boolean[] finalCheckedItems = checkedItems;
                final ArrayList<Integer> finalMUserItems1 = mUserItems;
                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < finalCheckedItems.length; i++) {
                            finalCheckedItems[i] = false;
                            finalMUserItems1.clear();
                            invitees.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                break;
            case R.id.otherSpeakers_createmeeting:
                final boolean[] checkedItems1;
                checkedItems1 = new boolean[listItems.length];
                final ArrayList<Integer> mUserItems1 = new ArrayList<>();
                AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(getActivity());
                mBuilder1.setTitle("Choose Other Speakers");
                mBuilder1.setMultiChoiceItems(listItems, checkedItems1, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserItems1.contains(position)) {
                                mUserItems1.add(position);
                            }
                        } else {
                            if (mUserItems1.contains(position)) {
                                mUserItems1.remove(mUserItems1.indexOf(position));
                            }
                        }

                    }
                });

                mBuilder1.setCancelable(false);
                mBuilder1.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        List<String> otherSpeakersInsert = new ArrayList<>();
                        for (int i = 0; i < mUserItems1.size(); i++) {
                            item = item + listItems[mUserItems1.get(i)];
                            otherSpeakersInsert.add(listItems[mUserItems1.get(i)]);
                            if (i != mUserItems1.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        otherSpeakers.setText(item);
                        conference.setOtherSpeakers(otherSpeakersInsert);
                    }
                });

                mBuilder1.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder1.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems1.length; i++) {
                            checkedItems1[i] = false;
                            mUserItems1.clear();
                            otherSpeakers.setText("");
                        }
                    }
                });

                AlertDialog mDialog1 = mBuilder1.create();
                mDialog1.show();
                break;
            case R.id.createMeeting_createmeeting:
                if (validate()){
                    startCreateMeeting();
                }
                break;
            case R.id.adress_createmeeting:
                Intent intent = new Intent(getActivity(),BaiduMapActivity.class);
                startActivityForResult(intent,REQUEST_MEETING_LOCATION);
                break;
        }
    }

    private List<String> getFriends(){
        List<String> friends = new ArrayList<>();
        friends.add("Jack");
        friends.add("Bob");
        friends.add("Tom");
        friends.add("John");
        friends.add("Tony");
        friends.add("Sam");
        friends.add("David");
        return friends;
    }

    private void startCreateMeeting() {
        conference.setTopic(topic.getText().toString());
        if (!description.getText().toString().isEmpty()){
            conference.setDescription(description.getText().toString());
        }
        if (!specificAddress.getText().toString().isEmpty()){
            conference.setSpecificAddress(specificAddress.getText().toString());
        }
        Log.d(TAG, "startCreateMeeting: " + conference);
        //发送请求
        /**
         * Todo
         */
    }

    public boolean validate() {
        boolean valid = true;

        String topicText = topic.getText().toString();
        String timeText = time.getText().toString();
        String addressText = address.getText().toString();

        if (topicText.isEmpty() || timeText.isEmpty() || addressText.isEmpty()) {
            valid = false;
        }
        return valid;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (meSpeaker.isChecked()){
            conference.setMeSpeaker(true);
        }else {
            conference.setMeSpeaker(false);
        }
        if (isPrivate.isChecked()){
            conference.setPrivate(true);
        }else{
            conference.setPrivate(false);
        }
    }
}
