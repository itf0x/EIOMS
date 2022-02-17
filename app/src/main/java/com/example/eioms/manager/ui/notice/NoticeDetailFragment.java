package com.example.eioms.manager.ui.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.eioms.Bean;
import com.example.eioms.R;

public class NoticeDetailFragment extends Fragment {

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
            Bean data = getArguments().getParcelable("DATA");
            TextView title = view.findViewById(R.id.tv_title);
            TextView username = view.findViewById(R.id.tv_username);
            TextView time = view.findViewById(R.id.tv_time);
            TextView content = view.findViewById(R.id.tv_content);

            time.setText(data.getTime());
            title.setText(data.getTitle());
            username.setText(data.getUsername());
            content.setText(data.getContent());
        }
        return view;
    }
}