package com.example.yosir.agenda;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AgendaContentActivity extends AppCompatActivity {

    public static void actionStart(Context context, String agendaTitle, String newsContent) {
        Intent intent = new Intent(context, AgendaContentActivity.class);
        intent.putExtra("agenda_title", agendaTitle);
        intent.putExtra("agenda_content", newsContent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_content);
        String agendaTitle = getIntent().getStringExtra("agenda_title"); // 获取传入的日程标题
        String agendaContent = getIntent().getStringExtra("agenda_content"); // 获取传入的日程内容
        AgendaContentFragment agendaContentFragment = (AgendaContentFragment) getSupportFragmentManager().findFragmentById(R.id.agenda_content_fragment);
        agendaContentFragment.refresh(agendaTitle, agendaContent); // 刷新AgendaContentFragment界面
    }

}
