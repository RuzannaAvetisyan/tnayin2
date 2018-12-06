package com.paypal.desk;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static final Connection connection = getConnection();

    private static Connection getConnection() {
        try {

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/paypal",
                    "root",
                    ""

            );

            System.out.println("Connection successful");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static int createUser(String firstName, String lastName) {
        String sql = "insert into users " +
                "(first_name, last_name)" +
                " values (" +
                "'" + firstName + "'" +
                ", " +
                "'" + lastName + "'" +
                ")";

        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);

            String idSql = "select max(id) from users";
            Statement idStatement = connection.createStatement();
            ResultSet resultSet = idStatement.executeQuery(idSql);

            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Updates the user balance in database
     * Sets balance = balance + amount
     *
     * @param userId id of the user in users table
     * @param amount double value of the amount to insert
     */
    static String cashFlow(int userId, double amount) {
        List<User> list = new ArrayList<>(listUsers());
        String s;
        boolean id = false;
        for(User user : list) if(userId == user.getId()) id = true;
        if(id){
            try {
                PreparedStatement statement = connection.prepareStatement("update users set balance = balance + ? where id = ?;");
                statement.setDouble(1,amount);
                statement.setInt(2, userId);
                int result = statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            s = "Balance changed successfully.";
            return s;
        }else{
            s = "Balance change is impossible.";
            return s;
        }

    }

    /**
     * Emulates a transaction between 2 users
     * Takes money from one account and adds to another account
     *
     * @param userFrom source user id
     * @param userTo   target user id
     * @param amount   transaction amount
     */
    static String transaction(int userFrom, int userTo, double amount) {
        List<User> list = new ArrayList<>(listUsers());
        String s;
        boolean from = false;
        boolean to = false;
        for(User user : list){
            if(userFrom == user.getId() && amount <= user.getBalance()) from = true;
            if(userTo == user.getId()) to = true;
        }
        if(from && to){
            PreparedStatement statement;
            String sql = "insert into transactions (user_from, user_to, transaction_amount) values ("
                    + userFrom + ", " + userTo + "," + amount + ")";
            try {
                statement = connection.prepareStatement("update users set balance = case when id = ? then balance - ?" +
                        "when id  = ? then balance + ? else balance end;");
                statement.setInt(1,userFrom);
                statement.setDouble(2,amount);
                statement.setInt(3,userTo);
                statement.setDouble(4,amount);
                boolean result = statement.execute();
                statement = connection.prepareStatement(sql);
                boolean transaction = statement.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            s = "Transaction successful";
            return s;
        }else{
            s = "Transaction is impossible";
            return s;
        }
    }

    static List<User> listUsers() {
        String sql = "select * from users";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<User> userList = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                double balance = resultSet.getDouble("balance");

                userList.add(new User(
                        id, firstName, lastName, balance
                ));
            }
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
