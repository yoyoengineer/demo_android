package com.example.yosir.agenda;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yosir.agenda.model.Agenda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AgendaTitleFragment extends Fragment {

    private boolean isTwoPane;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agenda_title_frag, container, false);
        RecyclerView agendaTitleRecyclerView = (RecyclerView) view.findViewById(R.id.agenda_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        agendaTitleRecyclerView.setLayoutManager(layoutManager);
        AgendaAdapter adapter = new AgendaAdapter(getAgendas());
        agendaTitleRecyclerView.setAdapter(adapter);
        return view;
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
        List<Agenda> agendaList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Agenda agenda = new Agenda();
            agenda.setTitle("Agenda title " + i);
            agenda.setContent(getRandomLengthContent("This is agenda content " + i + ". "));
            agendaList.add(agenda);
        }
        return agendaList;
    }

    private String getRandomLengthContent(String content) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(content);
        }
        return builder.toString();
    }

    class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

        private List<Agenda> magendaList;

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView agendaTitleText;

            public ViewHolder(View view) {
                super(view);
                agendaTitleText = (TextView) view.findViewById(R.id.agenda_title);
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
                    Agenda news = magendaList.get(holder.getAdapterPosition());
                    if (isTwoPane) {
                        AgendaContentFragment newsContentFragment = (AgendaContentFragment)
                                getFragmentManager().findFragmentById(R.id.agenda_content_fragment);
                        newsContentFragment.refresh(news.getTitle(), news.getContent());
                    } else {
                        AgendaContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Agenda news = magendaList.get(position);
            holder.agendaTitleText.setText(news.getTitle());
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
