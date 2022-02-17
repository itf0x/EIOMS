package com.example.eioms.student.ui.journal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eioms.DBOpenHelper;
import com.example.eioms.Data;
import com.example.eioms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalFragment extends Fragment {

    private EditText journal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.journal_fragment, container, false);

        FloatingActionButton sendbutten = view.findViewById(R.id.bt_send);
        journal = view.findViewById(R.id.et_journal);
        Data app = (Data)getActivity().getApplication();
        String username = app.getUsername();

        sendbutten.setOnClickListener(view1 -> {
            String text = journal.getText().toString();
            //开始上传
            SaveJournal saveJournal = new SaveJournal(text,username);
            Thread thread = new Thread(saveJournal);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Toast.makeText(getContext(), "日志上传成功", Toast.LENGTH_SHORT).show();
            //Snackbar.make(view, "日志上传成功", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            getActivity().onBackPressed();
        });

        return view;
    }
}

class SaveJournal implements Runnable{

    private final String text;
    private final String username;
    private final String time;


    public SaveJournal(String text, String username) {
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
        String sql = "INSERT INTO `journal` (content,user,time) VALUES('"+text+"','"+username+"','"+time+"')";
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