package com.example.eioms.manager.ui.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.eioms.Bean;
import com.example.eioms.DBOpenHelper;
import com.example.eioms.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NoticeDetailFragment extends Fragment {

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
                DelNotice delNotice = new DelNotice(data.getId());
                Thread thread = new Thread(delNotice);
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notice_detail_fragment, container, false);
        if (getArguments() != null) {
            data = getArguments().getParcelable("DATA");
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

class DelNotice implements Runnable{

    private final String id;

    public DelNotice(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        Connection conn;
        conn = DBOpenHelper.getConn();

        String sql = "DELETE FROM NOTICE WHERE ID = "+ id;
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