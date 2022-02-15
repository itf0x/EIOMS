package com.example.eioms.student.ui.notice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eioms.Bean;
import com.example.eioms.R;

public class NoticeDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    private Bean data;

    private TextView title;
    private TextView username;
    private TextView time;
    private TextView content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notice_detail_fragment, container, false);
        if (getArguments() != null) {
            data = getArguments().getParcelable("DATA");
            title = view.findViewById(R.id.tv_title);
            username = view.findViewById(R.id.tv_username);
            time = view.findViewById(R.id.tv_time);
            content = view.findViewById(R.id.tv_content);

            time.setText(data.getTime());
            title.setText(data.getTitle());
            username.setText(data.getUsername());
            content.setText(data.getContent());
        }
        return view;
    }
}