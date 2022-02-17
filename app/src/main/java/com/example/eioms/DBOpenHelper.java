package com.example.eioms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//链接mysql
public class DBOpenHelper {
    private static final String diver = "com.mysql.jdbc.Driver";
    //加入utf-8是为了后面往表中输入中文，表中不会出现乱码的情况
    private static final String url = "jdbc:mysql://192.168.2.192:3306/eioms?characterEncoding=utf-8";
    private static final String user = "root";//用户名
    private static final String password = "ww1999211";//密码
    /*
     * 连接数据库
     * */
    public static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(diver);
            conn = (Connection) DriverManager.getConnection(url,user,password);//获取连接
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
