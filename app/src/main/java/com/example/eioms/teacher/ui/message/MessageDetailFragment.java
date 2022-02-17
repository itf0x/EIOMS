package com.example.eioms.teacher.ui.message;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.eioms.Bean;
import com.example.eioms.R;
import com.example.eioms.teacher.ui.feedback.SendFeedbackFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MessageDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    private Bean data;

    private TextView content;
    private TextView username;
    private TextView time;
    private TextView reply;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feedback_detail_fragment, container, false);
        FloatingActionButton button = view.findViewById(R.id.bt_send);

        if (getArguments() != null) {
            data = getArguments().getParcelable("DATA");
            reply = view.findViewById(R.id.tv_reply);
            username = view.findViewById(R.id.tv_username);
            time = view.findViewById(R.id.tv_time);
            content = view.findViewById(R.id.tv_content);

            time.setText(data.getTime());
            reply.setText(data.getReply());
            username.setText(data.getUsername());
            content.setText(data.getContent());
        }

        if(data.getReply() == null || "".equals(data.getReply()))
        {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("DATA",data);
                    SendMessageFragment sendMessageFragment = new SendMessageFragment();
                    sendMessageFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.nav_host_fragment_content_teacher,sendMessageFragment,"send")
                            .addToBackStack(null).commit();
                }
            });
        }
        else {
            button.setVisibility(View.GONE);
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentByTag("comment");
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    return true;
                }

                return false;
            }
        });

        return view;
    }

}