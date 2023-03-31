package org.example;

import javax.xml.crypto.Data;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        String port = "3306";
        String databaseName = "tv3NewsDB";
        // Set username and password.
        String username = "root";
        String password = "Mypassword01";

        Scanner scan = new Scanner(System.in, "CP850");
        Database database = new Tv3Database();

        try {
            database.login(host, port, username, password, databaseName);
        } catch (SQLException e) {
            // TODO
        }

        String chosen;
        while (true) {
            System.out.println("To Insert press 1, To Query press 2, To Load File press 3");
            chosen = scan.next();
            scan.nextLine();

            switch (chosen) {
                case "1":
                    boolean inserting = true;
                    while (inserting) {
                        System.out.println("What do you want to insert?");
                        System.out.println("0. Go back");
                        System.out.println("1. Reporter");
                        System.out.println("2. Footage");

                        chosen = scan.next();
                        switch (chosen) {
                            case "0":
                                inserting = false;
                                break;
                            case "1":
                                System.out.println("Please enter the CPR number of the new reporter:");
                                String cprString = scan.nextLine();
                                scan.nextLine();

                                if (database.reporterExists(cprString)) {
                                    System.out.println("The CPR number you entered already exists in the database");
                                    System.out.println("Do you want to overwrite this data? (Y/n)");

                                    chosen = scan.next();
                                    if (chosen.equalsIgnoreCase("n")) {
                                        continue;
                                    }
                                }

                                System.out.println("Enter the firstname of the reporter:");
                                String firstName = scan.nextLine();
                                System.out.println("Enter the lastname of the reporter:");
                                String lastName = scan.nextLine();
                                System.out.println("Enter the street name of the reporter:");
                                String streetName = scan.nextLine();
                                System.out.println("Enter the civic number of the reporter");
                                int civicNumber = scan.nextInt();
                                System.out.println("Enter the zip code of the reporter");
                                int zipCode = scan.nextInt();
                                System.out.println("Enter the country of the reporter");
                                String country = scan.nextLine();
                                Reporter reporter = new Reporter(Integer.parseInt(cprString), firstName, lastName, streetName, civicNumber, zipCode, country);

                                if (database.updateReporter(reporter)) {
                                    System.out.println("Updated succesfully");
                                }
                                else {
                                    System.out.println("Something went wrong");
                                }
                                break;
                            case "2":
                                System.out.println("Please enter the title of the new footage:");
                                String footageTitle = scan.nextLine();

                                if (database.footageExists(footageTitle)) {
                                    System.out.println("The title you entered already exists in the database");
                                    System.out.println("Do you want to overwrite this data? (Y/n)");

                                    chosen = scan.next();
                                    if (chosen.equalsIgnoreCase("n")) {
                                        continue;
                                    }
                                }

                                System.out.println("Enter the date (yyyyMMdd) of the footage:");
                                String dateString = scan.nextLine();
                                LocalDate shootingDate = LocalDate.of(
                                        Integer.parseInt(dateString.substring(0, 4)),
                                        Integer.parseInt(dateString.substring(4, 6)),
                                        Integer.parseInt(dateString.substring(6))
                                );
                                System.out.println("Enter the duration of the footage:");
                                int duration = scan.nextInt();
                                Footage footage = new Footage(footageTitle, shootingDate, duration);

                                if (database.updateFootage(footage)) {
                                    System.out.println("Updated succesfully");
                                }
                                else {
                                    System.out.println("Something went wrong");
                                }
                                break;
                            default:
                                System.out.println("Please choose one of the listed options");
                                break;
                        }
                    }
                case "2":
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
                            System.out.print(resultSetMetaData.getColumnName(i) + "; ");
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
                                    System.out.print(resultSet.getString(i) + "; ");
                                }
                            }
                            System.out.println();
                        }
                    } catch (SQLException e) {
                        System.out.println("Something went wrong when executing your query");
                    }
                    break;
                case "3":
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
                    } catch (IOException e) {
                        // ignored
                    }
                    break;
                default:
                    System.out.println("YEE");
                    break;
            }
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