package com.example.yosir.agenda;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.yosir.agenda.model.Agenda;
import com.example.yosir.agenda.observers.AgendaAddFBClickObserver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<AgendaAddFBClickObserver> agendaAddFBClickObservers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                notifyAgendaAddFBClick(v);
            }
        });
    }

    public void addAgendaAddFBClickObserver(AgendaAddFBClickObserver agendaAddFBClickObserver){
        agendaAddFBClickObservers.add(agendaAddFBClickObserver);
    }

    private void notifyAgendaAddFBClick(View v){
        for (AgendaAddFBClickObserver a: agendaAddFBClickObservers) {
            a.onNotifyForFBClick(v);
        }
    }
}
