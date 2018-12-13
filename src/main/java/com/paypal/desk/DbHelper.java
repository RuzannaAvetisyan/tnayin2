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
        String sql = "insert into users (first_name, last_name)values (?,?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,firstName);
            statement.setString(2,lastName);
            statement.execute();

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
    static int cashFlow(int userId, double amount) {
        List<User> list = new ArrayList<>(listUsers());
        int result;
        boolean id = false;
        String sql = "update users set balance = balance + ? where id = ? and balance + ? >= 0;";
        for(User user : list) if(userId == user.getId()) id = true;
        if(id){
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setDouble(1,amount);
                statement.setInt(2, userId);
                statement.setDouble(3, amount);
                result = statement.executeUpdate();

            } catch (SQLException e) {
                result = 0;
                e.printStackTrace();
            }

            return result;
        }else{
            return 0;
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
    static String transaction(int userFrom, int userTo, double amount) throws SQLException {
        String s = null;

        try {
            connection.setAutoCommit(false);
            int from =cashFlow(userFrom, -amount);
            int to = cashFlow(userTo, amount);
            PreparedStatement statement;
            String sql = "insert into transactions (user_from, user_to, transaction_amount) values (?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userFrom);
            statement.setInt(2, userTo);
            statement.setDouble(3,amount);
            int transactionTable = statement.executeUpdate();
            if(from != 1 || to != 1 || transactionTable != 1) throw new SQLException();
            connection.commit();
            s = "Transaction successful";
        } catch (SQLException e) {
            s = "Transaction is impossible";
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            return s;
        }

//        List<User> list = new ArrayList<>(listUsers());
//        String s;
//        boolean from = false;
//        boolean to = false;
//        for(User user : list){
//            if(userFrom == user.getId() && amount <= user.getBalance()) from = true;
//            if(userTo == user.getId()) to = true;
//        }
//        if(from && to){
//            PreparedStatement statement;
//            String sql = "insert into transactions (user_from, user_to, transaction_amount) values ("
//                    + userFrom + ", " + userTo + "," + amount + ")";
//            try {
//                cashFlow(userFrom, -amount);
//                cashFlow(userTo, amount);
//                statement = connection.prepareStatement(sql);
//                boolean transaction = statement.execute();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            s = "Transaction successful";
//            return s;
//        }else{
//            s = "Transaction is impossible";
//            return s;
//        }
    }

    static List<User> listUsers() {
        String sql = "select * from users";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
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

    public static List<Transaction> listTransaction() {
        String sql = "select * from transactions";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);

            List<Transaction> transactionLisr = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userFrom = resultSet.getInt("user_from");
                int userTo = resultSet.getInt("user_to");
                double transactionAmount = resultSet.getDouble("transaction_amount");
                Timestamp transactionDate = resultSet.getTimestamp("transaction_date");

                transactionLisr.add(new Transaction(
                        id, userFrom, userTo, transactionAmount,transactionDate));
            }
            return transactionLisr;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
