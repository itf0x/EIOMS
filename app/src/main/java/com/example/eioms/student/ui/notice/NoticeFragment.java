package com.example.eioms.student.ui.notice;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eioms.Bean;
import com.example.eioms.DBOpenHelper;
import com.example.eioms.LoginActivity;
import com.example.eioms.R;
import com.example.eioms.student.StudentActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class NoticeFragment extends Fragment {
    private List<Bean> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_fragment_list, container, false);
        //异步查询服务器公告
        GetNotice getInfo = new GetNotice();
        Thread thread = new Thread(getInfo);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //取回数据
        data = getInfo.getData();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyItemRecyclerViewAdapter myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(data);
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
            myItemRecyclerViewAdapter.setRecyclerItemClickListener(new MyItemRecyclerViewAdapter.OnRecyclerItemClickListener() {
                @Override
                public void onRecyclerItemClick(int position) {
                    Toast.makeText(view.getContext(), "点击了"+position, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }
}

//连接数据库获取信息的异步类
class GetNotice implements Runnable{
    private List<Bean> data = new ArrayList<>();

    public List<Bean> getData() {
        return data;
    }

    @Override
    public void run() {
        Connection conn = DBOpenHelper.getConn();
        //获取数据库中公告信息
        String sql = "SELECT n.ID,n.`USER`,n.CONTENT,n.TIME,n.TITLE,u.`NAME` FROM notice as n LEFT JOIN user as u on u.`USER` = n.`USER`";
        Statement st;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                Bean bean = new Bean();
                bean.setId(rs.getString(1));
                bean.setUserid(rs.getString(2));
                bean.setContent(rs.getString(3));
                bean.setTime(rs.getString(4));
                bean.setTitle(rs.getString(5));
                bean.setUsername(rs.getString(6));
                data.add(bean);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}