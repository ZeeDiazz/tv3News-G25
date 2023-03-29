package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "localhost"; //host is "localhost" or "127.0.0.1"
        String port = "3306"; //port is where to communicate with the RDBMS
        String database = "tv3NewsDB"; //database containing tables to be queried
        String cp = "utf8"; //Database codepage supporting Danish (i.e. æøåÆØÅ)
        // Set username and password.
        String username = "root";		// Username for connection
        String password = "Mypassword01";	// Password for username
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=" + cp;
        try {
            Scanner scanner = new Scanner(System.in, "CP850"); //Western Europe Console CodePage
            // Get a connection.
            Connection connection = DriverManager.getConnection(url, username, password);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}