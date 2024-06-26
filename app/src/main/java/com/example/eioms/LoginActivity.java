package com.example.eioms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eioms.admin.AdminActivity;
import com.example.eioms.manager.ManagerActivity;
import com.example.eioms.student.StudentActivity;
import com.example.eioms.teacher.TeacherActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    private EditText etAccount, etId;
    private CheckBox cbSave, cbAutologin;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().setTitle("登录");

        sp = this.getSharedPreferences("Login", Context.MODE_PRIVATE);

        //查找界面元素
        etAccount = findViewById(R.id.et_username);
        etId = findViewById(R.id.et_id);
        Button btnLogin = findViewById(R.id.bt_login);
        cbAutologin = findViewById(R.id.cb_autologin);
        cbSave = findViewById(R.id.cb_rember);

        cbAutologin.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cbSave.setChecked(true);
                cbAutologin.setChecked(true);
            } else {
                cbAutologin.setChecked(false);
            }
        });

        cbSave.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                cbSave.setChecked(true);
            } else {
                cbSave.setChecked(false);
                cbAutologin.setChecked(false);
            }
        });

        if (sp.getBoolean("SAVE_ISCHECK", false)) {
            //设置默认是记录密码状态
            cbSave.setChecked(true);
            etAccount.setText(sp.getString("USERNAME", ""));
            etId.setText(sp.getString("ID", ""));
            //判断自动登陆多选框状态
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                //设置默认是自动登录状态
                cbAutologin.setChecked(true);

                btnLogin.callOnClick();
            }
        }
    }

    public void login(View view) throws InterruptedException {
        String account = etAccount.getText().toString();
        String id = etId.getText().toString();
        Data app = (Data) getApplication();
        //启动数据库查询异步线程并阻塞
        GetInfo getInfo = new GetInfo(account,id,app);
        Thread thread = new Thread(getInfo);
        thread.start();
        thread.join();

        int outh = getInfo.getRes();
        if (outh != -1) {
            //记录用户信息
            saveinfo();
            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            //根据不同身份切换页面
            switch (outh){
                case 0:
                    startActivity(new Intent(this, StudentActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(this, TeacherActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(this, ManagerActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(this, AdminActivity.class));
                    break;
            }


            finish();
        } else {
            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveinfo() {
        if (cbAutologin.isChecked()) {
            sp.edit().putBoolean("AUTO_ISCHECK", true).apply();
        } else {
            sp.edit().putBoolean("AUTO_ISCHECK", false).apply();
        }

        if (cbSave.isChecked()) {
            //记住用户名、密码
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("USERNAME", etAccount.getText().toString());
            editor.putString("ID", etId.getText().toString());
            editor.putBoolean("SAVE_ISCHECK", true);
            editor.apply();
        } else {
            sp.edit().clear().apply();
        }
    }

}

//连接数据库获取信息的异步类
class GetInfo implements Runnable{
    private final String number;
    private final String inputid;
    private final Data app;
    private int res;

    public int getRes() {
        return res;
    }

    GetInfo(String number, String id, Data app){
        this.number = number;
        this.inputid = id;
        this.app = app;
    }

    @Override
    public void run() {
        Connection conn;
        conn =(Connection) DBOpenHelper.getConn();
        //获取数据库中身份证信息
        String sql = "SELECT id from USER WHERE user='"+number+"'";
        Statement st;
        try {
            st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            String id = rs.getString(1);
            //如果与输入一致
            if(inputid.equals(id))
            {
                app.setUsername(number);
                app.setId(inputid);
                //获取用户身份
                sql = "SELECT AUTHORITY from USER WHERE user='"+number+"'";
                rs = st.executeQuery(sql);
                rs.next();
                String auth = rs.getString(1);

                //获取用户姓名
                sql = "SELECT name from USER WHERE user='"+number+"'";
                rs = st.executeQuery(sql);
                rs.next();
                String name = rs.getString(1);
                app.setName(name);

                res = Integer.parseInt(auth);
            }
            else
            {
                res = -1;
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}