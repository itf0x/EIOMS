package com.example.eioms.student.ui.message;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.eioms.DBOpenHelper;
import com.example.eioms.Data;
import com.example.eioms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendMessageFragment extends Fragment {

    private EditText feedback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.feedback_send_fragment, container, false);

        FloatingActionButton sendbutten = view.findViewById(R.id.bt_send);
        feedback = view.findViewById(R.id.et_feedback);
        Data app = (Data)getActivity().getApplication();
        String username = app.getUsername();

        sendbutten.setOnClickListener(view1 -> {
            String text = feedback.getText().toString();
            //开始上传
            SaveMessage saveMessage = new SaveMessage(text,username);
            Thread thread = new Thread(saveMessage);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Toast.makeText(getContext(), "日志上传成功", Toast.LENGTH_LONG).show();
        });

        //返回上一层
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

class SaveMessage implements Runnable{

    private final String text;
    private final String username;
    private final String time;


    public SaveMessage(String text, String username) {
        this.text = text;
        this.username = username;
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        this.time = ft.format(date);
    }

    @Override
    public void run() {
        Connection conn;
        conn = DBOpenHelper.getConn();
        String sql = "INSERT INTO `MESSAGE` (content,user,time) VALUES('"+text+"','"+username+"','"+time+"')";
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

