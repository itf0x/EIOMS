package com.example.eioms.manager.ui.feedback;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.eioms.Bean;
import com.example.eioms.DBOpenHelper;
import com.example.eioms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackDetailFragment extends Fragment {

    private Bean data;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delbuttom,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.delete://监听菜单按钮
                // 功能代码
                DelFeedback delFeedback = new DelFeedback(data.getId());
                Thread thread = new Thread(delFeedback);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feedback_detail_fragment, container, false);
        FloatingActionButton button = view.findViewById(R.id.bt_send);

        if (getArguments() != null) {
            data = getArguments().getParcelable("DATA");
            TextView reply = view.findViewById(R.id.tv_reply);
            TextView username = view.findViewById(R.id.tv_username);
            TextView time = view.findViewById(R.id.tv_time);
            TextView content = view.findViewById(R.id.tv_content);

            time.setText(data.getTime());
            reply.setText(data.getReply());
            username.setText(data.getUsername());
            content.setText(data.getContent());
        }

        if(data.getReply() != null)
        {
            button.setVisibility(View.GONE);
        }
        else {
            button.setOnClickListener(view1 -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA",data);
                SendFeedbackFragment sendFeedbackFragment = new SendFeedbackFragment();
                sendFeedbackFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.nav_host_fragment_content_manager,sendFeedbackFragment,"comment")
                        .addToBackStack(null).commit();
            });
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((view12, i, keyEvent) -> {
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

class DelFeedback implements Runnable{

    private final String id;

    public DelFeedback(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        Connection conn;
        conn = DBOpenHelper.getConn();

        String sql = "DELETE FROM FEEDBACK WHERE ID = "+ id;
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);
            //插入到数据库中
            pst.executeUpdate();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}