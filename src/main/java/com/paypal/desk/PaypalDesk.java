package com.paypal.desk;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

public class PaypalDesk {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to paypal");
        System.out.println("Enter command");

        while (true) {
            System.out.println("(B) -> Create DB(If you have a database with the name \"paypal\"\n" +
                    " it will be deleted and the eponymous empty one will be created.)");
            System.out.println("(C) -> Create user");
            System.out.println("(LU) -> List users");
            System.out.println("(LT) -> List transactions");
            System.out.println("(+) -> Cash in");
            System.out.println("(-) -> Cash out");
            System.out.println("(T) -> Transaction");
            System.out.println("(Q) -> Quit");

            String command = scanner.nextLine();

            switch (command) {
                case "B":
                    createDB();
                    break;
                case "C":
                    createUser();
                    break;
                case "LU":
                    listUsers();
                    break;
                case "LT":
                    listTransactions();
                    break;
                case "+":
                    cashIn();
                    break;
                case "-":
                    cashOut();
                    break;
                case "T":
                    transaction();
                    break;
                case "Q":
                    return;
            }
        }
    }

    private static void listTransactions() {
        List<Transaction> transactions = DbHelper.listTransaction();
        if (transactions == null) return;

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

    }

    private static void createDB() {
       boolean b = CreateDB.createDB();
       if(b == false)
           System.out.println("DB created successfully");
       else
           System.out.println("Error while creating the DB");
    }

    private static void createUser() {
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        int userId = DbHelper.createUser(
                firstName, lastName
        );

        if (userId != -1) {
            System.out.println(
                    MessageFormat.format(
                            "User {0} created successfully",
                            userId
                    )
            );
        } else {
            System.out.println(
                    "Error while creating the user"
            );
        }
    }

    private static void listUsers() {
        List<User> users = DbHelper.listUsers();
        if (users == null) return;

        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void cashIn() {
        int userId = getUserIdFromConsole("User id: ");
        double amount = getAmountFromConsole();

        int i = DbHelper.cashFlow(userId, amount);
        if (i == 1) System.out.println("Balance successfully changed.");
        else System.out.println("Balance change is impossible.");
    }

    private static void cashOut() {
        int userId = getUserIdFromConsole("User id: ");
        double amount = getAmountFromConsole();

        int i = DbHelper.cashFlow(userId, -amount);
        if (i == 1) System.out.println("Balance successfully changed.");
        else System.out.println("Balance change is impossible.");
    }

    private static void transaction() {
        int userFrom = getUserIdFromConsole(
                "User id: "
        );
        int userTo = getUserIdFromConsole(
                "Target user id: "
        );

        double amount = getAmountFromConsole();

        String s = null;
        try {
            s = DbHelper.transaction(
                    userFrom, userTo, amount
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }


    private static int getUserIdFromConsole(String message) {
        System.out.print(message);
        return Integer.parseInt(
                scanner.nextLine()
        );
    }

    private static double getAmountFromConsole() {
        System.out.println("Amount: ");
        return Double.parseDouble(
                scanner.nextLine()
        );
    }
}
