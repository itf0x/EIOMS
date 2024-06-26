package com.example.eioms.teacher.ui.feedback;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eioms.Bean;
import com.example.eioms.DBOpenHelper;
import com.example.eioms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class FeedbackFragment extends Fragment {

    private List<Bean> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_fragment_list, container, false);
        FloatingActionButton button = view.findViewById(R.id.bt_newfeedback);
        button.setVisibility(View.GONE);

        //异步查询服务器公告
        GetFeedback getFeedback = new GetFeedback();
        Thread thread = new Thread(getFeedback);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //取回数据
        data = getFeedback.getData();

        // Set the adapter
        if (view instanceof CoordinatorLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            MyItemRecyclerViewAdapter myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(data);
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
            myItemRecyclerViewAdapter.setRecyclerItemClickListener(position -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA",data.get(position));
                FeedbackDetailFragment feedbackDetailFragment = new FeedbackDetailFragment();
                feedbackDetailFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.nav_host_fragment_content_teacher, feedbackDetailFragment, "comment")
                        .addToBackStack(null)
                        .commit();

            });
        }

        return view;
    }
}

//连接数据库获取信息的异步类
class GetFeedback implements Runnable{
    private final List<Bean> data = new ArrayList<>();

    public List<Bean> getData() {
        return data;
    }

    @Override
    public void run() {
        Connection conn = DBOpenHelper.getConn();
        //获取数据库中公告信息
        String sql = "SELECT f.ID,f.`USER`,f.CONTENT,f.TIME,f.REPLY,u.`NAME` FROM FEEDBACK as f LEFT JOIN USER as u on f.`USER` = u.`USER`";
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
                bean.setReply(rs.getString(5));
                bean.setUsername(rs.getString(6));
                data.add(bean);
            }
            Collections.reverse(data);
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}