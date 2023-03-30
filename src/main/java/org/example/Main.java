package org.example;

import java.sql.*;
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
            // Get a connection.
            Connection connection = DriverManager.getConnection(url, username, password);

            Scanner scan = new Scanner(System.in, "CP850");
            System.out.println("To Insert press 1, To Query press 2");
            int Options = scan.nextInt();
            scan.nextLine();

            Statement statement = connection.createStatement();
            switch(Options) {
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
                            };
                        }
                        System.out.println();
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
    }

    public static void createJournalist(){



    }

}