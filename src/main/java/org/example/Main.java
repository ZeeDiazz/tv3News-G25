package org.example;

import javax.xml.crypto.Data;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        String port = "3306";
        String databaseName = "tv3NewsDB";
        String cp = "utf8";
        // Set username and password.
        String username = "root";
        String password = "Mypassword01";

        Scanner scan = new Scanner(System.in, "CP850");
        Database database = new Tv3Database();

        try {
            database.login(host, port, username, password, databaseName);
        }
        catch (SQLException e) {
            // TODO
        }

        while (true) {
            System.out.println("To Insert press 1, To Query press 2, To Load File press 3");
            int chosen = scan.nextInt();
            scan.nextLine();

            switch(chosen) {
                case 1:
                    // TODO make sure it handles specifically footage and reporters
                    System.out.println("Insert values in tables:");
                    String SQLInsertion = scan.nextLine();
                    // statement.executeUpdate(SQLInsertion);
                    break;
                case 2:
                    System.out.println("Type SQL Query:");
                    System.out.println("");
                    String query = scan.nextLine();
                    ResultSet resultSet;
                    try {
                        resultSet = database.executeSanitisedQuery(query);

                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        int columnCount = resultSetMetaData.getColumnCount();

                        // Print all attribute names.
                        // --------------------------
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(resultSetMetaData.getColumnName(i)+"; ");
                        }
                        System.out.println();
                        System.out.println("------");

                        // Print all table rows.
                        // ---------------------
                        resultSet.beforeFirst(); // Set pointer for resultSet.next()
                        while (resultSet.next()) {
                            // Print all values in a row.
                            // --------------------------
                            for (int i = 1; i <= columnCount; i++) {
                                if (resultSet.getString(i) == null) {
                                    System.out.print("null; ");
                                } else {
                                    System.out.print(resultSet.getString(i)+"; ");
                                }
                            }
                            System.out.println();
                        }
                    }
                    catch (SQLException e) {
                        System.out.println("Something went wrong when executing your query");
                        continue;
                    }

                    break;
                case 3:
                    System.out.println("Enter filename:");
                    String filename = scan.nextLine();
                    if (!filename.endsWith(".csv")) {
                        filename += ".csv";
                    }
                    filename = "src/main/resources/" + filename;
                    FootagesAndReportersLoader loader = new FootagesAndReportersLoader();
                    try {
                        List<FootageAndReporter> footagesAndReporters = loader.loadFootagesAndReporters(filename);
                        for (FootageAndReporter footageAndReporter : footagesAndReporters) {
                            database.updateReporter(footageAndReporter.getReporter());
                            database.updateFootage(footageAndReporter.getFootage());
                        }
                    }
                    catch (IOException e) {
                        // ignored
                    }
                    break;
                default:
                    System.out.println("YEE");
                    break;
            }
        }

        /*
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?characterEncoding=" + cp;
        try {
            // Get a connection.
            Connection connection = DriverManager.getConnection(url, username, password);



            Statement statement = connection.createStatement();
            switch(chosen) {
                case 1:
                    System.out.println("Insert values in tables:");
                    String SQLInsertion = scan.nextLine();
                    statement.executeUpdate(SQLInsertion);
                    break;
                case 2:
                    System.out.println("Type SQL Query:");
                    System.out.println("");
                    String SQLQuery = scan.nextLine();

                    ResultSet resultSet = statement.executeQuery(SQLQuery);
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    int columnCount = resultSetMetaData.getColumnCount();

                    // Print all attribute names.
                    // --------------------------
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(resultSetMetaData.getColumnName(i)+"; ");
                    }
                    System.out.println();
                    System.out.println("------");

                    // Print all table rows.
                    // ---------------------
                    resultSet.beforeFirst(); // Set pointer for resultSet.next()
                    while (resultSet.next()) {
                        // Print all values in a row.
                        // --------------------------
                        for (int i = 1; i <= columnCount; i++) {
                            if (resultSet.getString(i) == null) {
                                System.out.print("null; ");
                            } else {
                                System.out.print(resultSet.getString(i)+"; ");
                            }
                        }
                        System.out.println();
                    }
                    break;
                case 3:
                    System.out.println("Enter filename:");
                    String filename = scan.nextLine();
                    if (!filename.endsWith(".csv")) {
                        filename += ".csv";
                    }
                    filename = "src/main/resources/" + filename;
                    FootagesAndReportersLoader loader = new FootagesAndReportersLoader();
                    try {
                        List<FootageAndReporter> footagesAndReporters = loader.loadFootagesAndReporters(filename);
                    }
                    catch (IOException e) {
                        // ignored
                    }
                    break;
                default:
                    System.out.println("YEE");
                    break;
            }
            statement.close();
            connection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
         */
    }
}