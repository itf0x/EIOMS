package com.example.eioms.student.ui.info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eioms.DBOpenHelper;
import com.example.eioms.Data;
import com.example.eioms.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InfoFragment extends Fragment {
    private EditText username;
    private EditText name;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);
        // Inflate the layout for this fragment
        Data app = (Data)getActivity().getApplication();

        EditText id = view.findViewById(R.id.et_id);
        username = view.findViewById(R.id.et_username);
        name = view.findViewById(R.id.et_name);
        Button save = view.findViewById(R.id.bt_save);

        id.setText(app.getId());
        username.setText(app.getUsername());
        name.setText(app.getName());
        save.setOnClickListener(view1 -> {
            SaveInfo saveInfo = new SaveInfo(name.getText().toString(),username.getText().toString());
            Thread thread = new Thread(saveInfo);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(), "保存成功,重启应用后生效", Toast.LENGTH_LONG).show();
        });

        return view;
    }
}

class SaveInfo implements Runnable{

    private final String name;
    private final String username;


    public SaveInfo(String name, String username) {
        this.name = name;
        this.username = username;
    }

    @Override
    public void run() {
        Connection conn;
        conn =(Connection) DBOpenHelper.getConn();
        //获取数据库中身份证信息
        String sql = "UPDATE `user` SET `NAME`='"+name+"' WHERE `USER` = '"+username+"'";
        PreparedStatement pst;
        try {
            pst = (PreparedStatement) conn.prepareStatement(sql);
            pst.executeUpdate();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}