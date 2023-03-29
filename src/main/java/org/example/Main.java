package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        String port = "3306";
        String database = "tv3NewsDB";
        String cp = "utf8";
        // Set username and password.
        String username = "root";
        String password = "Mypassword01";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=" + cp;
        try {
            Scanner scan = new Scanner(System.in, "CP850");

            // Get a connection.
            Connection connection = DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();

            System.out.println("Insert values in tables:");
            String SQLInsertion = scan.nextLine();
            statement.execute(SQLInsertion);

            statement.close();
            connection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}