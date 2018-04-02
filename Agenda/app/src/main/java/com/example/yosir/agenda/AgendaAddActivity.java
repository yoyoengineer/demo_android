package com.example.yosir.agenda;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.yosir.agenda.fragment.AgendaAddFragment;
import com.example.yosir.agenda.model.Agenda;

import java.util.List;


public class AgendaAddActivity extends AppCompatActivity {
    public static final String TAG = "AgendaAddActivity";
    private boolean saved = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void actionStart(Context context, ActivityOptionsCompat options) {
        Intent intent = new Intent(context, AgendaAddActivity.class);
//        intent.putExtra("agenda_title", agendaTitle);
//        intent.putExtra("agenda_content", agendaContent);
        context.startActivity(intent,options.toBundle());
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);//设置返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                finish();
            }
        });//返回监听

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        String agendaTitle = getIntent().getStringExtra("agenda_title"); // 获取传入的日程标题
//        String agendaContent = getIntent().getStringExtra("agenda_content"); // 获取传入的日程内容
        final AgendaAddFragment agendaAddFragment = (AgendaAddFragment) getSupportFragmentManager().findFragmentById(R.id.agenda_add_fragment);
        agendaAddFragment.refresh(); // 刷新AgendaContentFragment界面
        final FloatingActionButton fab = findViewById(R.id.save_agenda_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saved){
                    onBackPressed();
//                    AgendaAddActivity.this.finish();
                }else {
                    if (agendaAddFragment.saveAgenda() > 0){
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.back_fab));
                        saved = true;
                    }
                    List<Agenda> agendaList = Agenda.listAll(Agenda.class);
                    for (Agenda a:agendaList) {
                        Log.d(TAG, "onClick: " + a.toString());
                    }
                }
            }
        });


    }
}
