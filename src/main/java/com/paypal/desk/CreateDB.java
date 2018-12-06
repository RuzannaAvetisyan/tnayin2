package com.paypal.desk;

import java.sql.*;

public class CreateDB {
    public static boolean createDB() {
        Connection conn;
        PreparedStatement s;
        boolean Result = true;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=");
            s = conn.prepareStatement("drop database if exists paypal;");
            Result = s.execute();
            s = conn.prepareStatement("create database paypal;");
            Result = s.execute();
            s = conn.prepareStatement("use paypal;");
            Result = s.execute();
            s = conn.prepareStatement("drop table if exists users;");
            Result = s.execute();
            s = conn.prepareStatement("drop table if exists transactions;");
            Result = s.execute();
            s = conn.prepareStatement("create table users (" +
                    "  id serial unique," +
                    "  first_name text not null," +
                    "  last_name text not null," +
                    "  balance real not null default 0);");
            Result = s.execute();
            s = conn.prepareStatement("create table transactions (" +
                    "  id serial unique," +
                    "  user_from int," +
                    "  user_to int," +
                    "  transaction_amount real not null," +
                    "  transaction_date timestamp not null default now());");
            Result = s.execute();
        }catch (SQLException e){
            Result = true;
        }
        return  Result;
    }
}
