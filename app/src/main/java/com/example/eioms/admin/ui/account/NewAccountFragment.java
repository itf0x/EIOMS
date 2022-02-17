package com.example.eioms.admin.ui.account;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.eioms.DBOpenHelper;
import com.example.eioms.Data;
import com.example.eioms.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewAccountFragment extends Fragment {
    private Button save;
    private EditText username;
    private EditText id;
    private EditText name;
    private Spinner auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_detail_fragment, container, false);
        username = view.findViewById(R.id.et_username);
        id = view.findViewById(R.id.et_id);
        name = view.findViewById(R.id.et_name);
        auth = view.findViewById(R.id.spinner);
        save = view.findViewById(R.id.bt_save);

        save.setOnClickListener(view1 -> {
            NewUser newUser = new NewUser(name.getText().toString(), username.getText().toString(), id.getText().toString(), auth.getSelectedItem().toString());
            Thread thread = new Thread(newUser);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener((view1, i, keyEvent) -> {
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("comment");
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                return true;
            }

            return false;
        });

        return view;
    }
}

class NewUser implements Runnable{

    private final String name;
    private final String username;
    private final String id;
    private final String authority;


    public NewUser(String name, String username, String id, String authority) {
        this.name = name;
        this.username = username;
        this.id = id;
        switch (authority){
            case "教学督导人员":
                this.authority ="1";
                break;
            case "教学管理人员":
                this.authority ="2";
                break;
            case "管理员":
                this.authority ="3";
                break;
            default:
                this.authority ="0";
        }
    }

    @Override
    public void run() {
        Connection conn;
        conn =(Connection) DBOpenHelper.getConn();
        //获取数据库中身份证信息

        String sql = "INSERT INTO (ID,AUTHORITY,NAME,user) VALUES("+id+","+authority+",'"+name+"',"+username+")";
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