package com.example.yosir.agenda.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yosir.agenda.AgendaAddActivity;
import com.example.yosir.agenda.AgendaContentActivity;
import com.example.yosir.agenda.MainActivity;
import com.example.yosir.agenda.R;
import com.example.yosir.agenda.model.Agenda;
import com.example.yosir.agenda.observers.AgendaAddFBClickObserver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AgendaTitleFragment extends Fragment implements AgendaAddFBClickObserver{

    private boolean isTwoPane;
    public static final String TRANSITION_FAB = "fab_transition";
    private Calendar mCalendar;
    private View titleFragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        titleFragmentView = inflater.inflate(R.layout.agenda_title_frag, container, false);

        AppCompatActivity activity= (AppCompatActivity) getActivity();
        if (activity instanceof MainActivity ){
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.addAgendaAddFBClickObserver(this);
        }
        return titleFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCalendar = Calendar.getInstance();
        RecyclerView agendaTitleRecyclerView = (RecyclerView) titleFragmentView.findViewById(R.id.agenda_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        agendaTitleRecyclerView.setLayoutManager(layoutManager);
        final AgendaAdapter adapter = new AgendaAdapter(getAgendas());
        agendaTitleRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.agenda_content_layout) != null) {
            isTwoPane = true; // 可以找到agenda_content_layout布局时，为双页模式
        } else {
            isTwoPane = false; // 找不到agenda_content_layout布局时，为单页模式
        }
    }

    private List<Agenda> getAgendas() {
        List<Agenda> agendaList = Agenda.listAll(Agenda.class);
        return agendaList;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onNotifyForFBClick(View view) {
        Pair<View, String> pair = Pair.create(view.findViewById(R.id.fab), TRANSITION_FAB);

        ActivityOptionsCompat options;
        Activity act = getActivity();
        options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, pair);


        if (isTwoPane) {
            AgendaAddFragment agendaAddFragment = (AgendaAddFragment)
                    getFragmentManager().findFragmentById(R.id.agenda_add_fragment);
            agendaAddFragment.refresh();
        } else {
            AgendaAddActivity.actionStart(getActivity(),options);
        }
    }

    class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

        private List<Agenda> magendaList;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

            TextView agendaTitleText;
            TextView agendaStartTimeText;
            TextView agendaEndTimeText;
            TextView agendaDateText;

            public ViewHolder(View view) {
                super(view);
                agendaTitleText = (TextView) view.findViewById(R.id.agenda_title);
                agendaStartTimeText = (TextView) view.findViewById(R.id.start_time);
                agendaEndTimeText = (TextView) view.findViewById(R.id.end_time);
                agendaDateText = (TextView) view.findViewById(R.id.date);
            }

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }


        }

        public AgendaAdapter(List<Agenda> newsList) {
            magendaList = newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_view_holder, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Agenda agenda = magendaList.get(holder.getAdapterPosition());
                    if (isTwoPane) {
                        AgendaContentFragment agendaContentFragment = (AgendaContentFragment)
                                getFragmentManager().findFragmentById(R.id.agenda_content_fragment);
                        agendaContentFragment.refresh(agenda);
                    } else {
                        AgendaContentActivity.actionStart(getActivity(), agenda);
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Agenda agenda = magendaList.get(position);
            System.out.println(agenda);
            if (agenda != null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MM dd, yyyy.");
                mCalendar.setTime(agenda.getStartTime());
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minutes = mCalendar.get(Calendar.MINUTE);
                String startTime = ""+hour + ":" + minutes+"";
                mCalendar.setTime(agenda.getEndTime());
                int endHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int endMinutes = mCalendar.get(Calendar.MINUTE);
                String endTime = ""+endHour + ":" + endMinutes+"";
                holder.agendaTitleText.setText(agenda.getTitle());
                holder.agendaDateText.setText(simpleDateFormat.format(agenda.getDate()));
                holder.agendaStartTimeText.setText(startTime);
                holder.agendaEndTimeText.setText(endTime);
            }
        }

        @Override
        public int getItemCount() {
            return magendaList.size();
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onViewAttachedToWindow(ViewHolder viewHolder) {
            super.onViewAttachedToWindow(viewHolder);
            animateCircularReveal(viewHolder.itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void animateCircularReveal(View view) {
            int centerX = 0;
            int centerY = 0;
            int startRadius = 0;
            int endRadius = Math.max(view.getWidth(), view.getHeight());
            Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
            view.setVisibility(View.VISIBLE);
            animation.start();
        }
    }

}
