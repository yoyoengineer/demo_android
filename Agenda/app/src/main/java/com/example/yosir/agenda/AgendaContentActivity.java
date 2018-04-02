package com.example.yosir.agenda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.yosir.agenda.fragment.AgendaContentFragment;
import com.example.yosir.agenda.model.Agenda;

import java.util.List;

public class AgendaContentActivity extends AppCompatActivity {

    private Agenda agenda;
    private AgendaContentState agendaContentState = AgendaContentState.BEFOREEDIT;

    public static void actionStart(Context context, Agenda agenda) {
        Intent intent = new Intent(context, AgendaContentActivity.class);
        intent.putExtra("agenda", agenda);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);//设置返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });//返回监听
        agenda = (Agenda) getIntent().getSerializableExtra("agenda"); // 获取传入的日程
        final AgendaContentFragment agendaContentFragment = (AgendaContentFragment) getSupportFragmentManager().findFragmentById(R.id.agenda_content_fragment);
        agendaContentFragment.refresh(agenda); // 刷新AgendaContentFragment界面
        final FloatingActionButton fab = findViewById(R.id.edit_agenda_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agendaContentState == AgendaContentState.BEFOREEDIT){
                    agendaContentState = AgendaContentState.EDITING;
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
                    agendaContentFragment.setEditable(true);
                }else if(agendaContentState == AgendaContentState.EDITING){
                    agendaContentState = AgendaContentState.BEFOREEDIT;
                    long saveResult = 0;
                    saveResult = agendaContentFragment.saveAgenda();
                    if (saveResult > 0){
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_agenda:
                askConfirmationForDelete();
        }
        return true;
    }

    private void askConfirmationForDelete() {
        final String titleOfAgendaToBeDeleted = agenda.getTitle();

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(AgendaContentActivity.this);
        alertdialog.setTitle("delete " + titleOfAgendaToBeDeleted + "?");
        alertdialog.setMessage("are you sure you want to delete " + titleOfAgendaToBeDeleted + "?");

        alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (agenda.delete()){
                    finish();
                }else {
                    Toast.makeText(AgendaContentActivity.this,"Failed to delete the agenda.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();
    }
}
