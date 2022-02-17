package com.example.eioms.manager.ui.notice;

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
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendNoticeFragment extends Fragment {

    private EditText notice;
    private EditText title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.notice_send_fragment, container, false);

        FloatingActionButton sendbutten = view.findViewById(R.id.bt_send);
        notice = view.findViewById(R.id.et_notice);
        title = view.findViewById(R.id.et_title);
        Data app = (Data)getActivity().getApplication();
        String username = app.getUsername();


        sendbutten.setOnClickListener(view1 -> {
            String noticetext = notice.getText().toString();
            String titiletext = title.getText().toString();
            //开始上传
            if("".equals(noticetext) || "".equals(titiletext)){
                Snackbar.make(view,"内容不能为空",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                return;
            }
            SaveNotice saveMessage = new SaveNotice(noticetext,titiletext,username);
            Thread thread = new Thread(saveMessage);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Toast.makeText(getContext(), "公告发布成功", Toast.LENGTH_LONG).show();
        });

        //返回上一层
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((view12, i, keyEvent) -> {
            Log.d("123","back");
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("send");
                getActivity().getSupportFragmentManager()
                        .beginTransaction().remove(fragment).commit();
                return true;
            }

            return false;
        });

        return view;
    }
}

class SaveNotice implements Runnable{

    private final String noticetext;
    private final String titiletext;
    private final String username;
    private final String time;


    public SaveNotice(String noticetext, String titiletext, String username) {
        this.noticetext = noticetext;
        this.username = username;
        this.titiletext = titiletext;
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        this.time = ft.format(date);
    }

    @Override
    public void run() {
        Connection conn;
        conn = DBOpenHelper.getConn();
        String sql = "INSERT INTO `NOTICE` (content,user,time,title) VALUES('"+noticetext+"','"+username+"','"+time+"','"+titiletext+"')";
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

