package com.example.yosir.agenda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AgendaContentFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.agenda_content_frag, container, false);
        return view;
    }

    public void refresh(String newsTitle, String newsContent) {
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView agendaTitleText = (TextView) view.findViewById (R.id.agenda_title);
        TextView agendaContentText = (TextView) view.findViewById(R.id.agenda_content);
        agendaTitleText.setText(newsTitle); // 刷新日程的标题
        agendaContentText.setText(newsContent); // 刷新日程的内容
    }

}
