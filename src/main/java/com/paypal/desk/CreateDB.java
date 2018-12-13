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
            s = conn.prepareStatement("create database if not exists paypal;");
            Result = s.execute();
            s = conn.prepareStatement("use paypal;");
            Result = s.execute();
            s = conn.prepareStatement("drop table if exists users;");
            Result = s.execute();
            s = conn.prepareStatement("drop table if exists transactions;");
            Result = s.execute();
            s = conn.prepareStatement("create table users (" +
                    "  id int auto_increment unique," +
                    "  first_name text not null," +
                    "  last_name text not null," +
                    "  balance real not null default 0, " +
                    "  primary key (id));");
            Result = s.execute();
            s = conn.prepareStatement("create table transactions (" +
                    "  id int auto_increment unique," +
                    "  user_from int," +
                    "  user_to int," +
                    "  transaction_amount real not null," +
                    "  transaction_date timestamp not null default now()," +
                    "  index (user_from)," +
                    "  foreign key (user_from)" +
                    "  references users(id)" +
                    "  on delete cascade," +
                    "  index (user_to)," +
                    "  foreign key (user_to)" +
                    "  references users(id)" +
                    "  on update cascade on delete cascade)engine=innodb;");
            Result = s.execute();
        }catch (SQLException e){
            Result = true;
        }
        return  Result;
    }
}
