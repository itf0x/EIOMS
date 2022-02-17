package com.example.eioms.admin.ui.account;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eioms.DBOpenHelper;
import com.example.eioms.Data;
import com.example.eioms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class AccountFragment extends Fragment {

    private List<Data> dataList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment_list, container, false);
        FloatingActionButton button = view.findViewById(R.id.bt_newuser);

        //异步查询服务器公告
        GetUser getUser = new GetUser();
        Thread thread = new Thread(getUser);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //取回数据
        dataList = getUser.getData();


        // Set the adapter
        if (view instanceof CoordinatorLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.rv_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            MyAccountRecyclerViewAdapter myAccountRecyclerViewAdapter = new MyAccountRecyclerViewAdapter(dataList);
            recyclerView.setAdapter(myAccountRecyclerViewAdapter);
            myAccountRecyclerViewAdapter.setRecyclerAccountClickListener(position -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA",dataList.get(position));
                AccountDetailFragment accountDetailFragment = new AccountDetailFragment();
                accountDetailFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.nav_host_fragment_content_admin, accountDetailFragment, "comment")
                        .addToBackStack(null)
                        .commit();

            });
        }

        button.setOnClickListener(view1 -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_fragment_content_admin,new NewAccountFragment(),"comment")
                .addToBackStack(null).commit());

        return view;
    }
}

//连接数据库获取信息的异步类
class GetUser implements Runnable{
    private final List<Data> dataList = new ArrayList<>();

    public List<Data> getData() {
        return dataList;
    }

    @Override
    public void run() {
        Connection conn = DBOpenHelper.getConn();
        //获取数据库中公告信息
        String sql = "SELECT * FROM `user`";
        Statement st;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                Data data = new Data();

                data.setUsername(rs.getString(1));
                data.setId(rs.getString(2));
                data.setAuthority(rs.getString(3));
                data.setName(rs.getString(4));
                dataList.add(data);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}