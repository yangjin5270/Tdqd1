package com.td.tdqd.util;
import android.util.Log;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBserverices implements Runnable{


    public static Connection connection;
    public static Statement statement;

    static{
        System.out.println("start db init");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection =  DriverManager.getConnection("jdbc:mysql://47.92.24.210:3306/shop_data?characterEncoding=utf-8","root","yangjin@2019");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("start db end");

    }
    public DBserverices(String sqlStr){
        this.sqlStr = sqlStr;
    }
    public String sqlStr;
    public int state;
    @Override
    public void run() {

        try {
            state = statement.executeUpdate(sqlStr);
        }
        catch (SQLException e) {
            if(e.toString().contains("Duplicate entry ")){
                Log.i("Database","Duplicate entry");
            }else{
                e.printStackTrace();
            }

        }
    }

    public static void closeConnection(){
        System.out.println("关闭数据链接");
        try {
            statement.close();
            connection.close();
            statement=null;
            connection=null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
    }


}
