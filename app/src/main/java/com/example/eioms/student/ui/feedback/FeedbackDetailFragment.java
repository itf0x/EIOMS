package com.example.eioms.student.ui.feedback;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.eioms.Bean;
import com.example.eioms.R;

public class FeedbackDetailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feedback_detail_fragment, container, false);
        if (getArguments() != null) {
            Bean data = getArguments().getParcelable("DATA");
            TextView reply = view.findViewById(R.id.tv_reply);
            TextView username = view.findViewById(R.id.tv_username);
            TextView time = view.findViewById(R.id.tv_time);
            TextView content = view.findViewById(R.id.tv_content);

            time.setText(data.getTime());
            reply.setText(data.getReply());
            username.setText(data.getUsername());
            content.setText(data.getContent());
        }
        view.findViewById(R.id.bt_send).setVisibility(View.GONE);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((view1, i, keyEvent) -> {
            Log.d("123","back");
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("comment");
                getActivity().getSupportFragmentManager()
                        .beginTransaction().remove(fragment).commit();

                return true;
            }

            return false;
        });

        return view;
    }

}