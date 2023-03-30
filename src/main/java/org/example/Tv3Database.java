package org.example;

import java.sql.*;


public class Tv3Database implements Database {


    Connection connection;
    Statement statement;

    @Override
    public void login(String host, String port, String username, String password, String databaseName) throws SQLException {
        login(host, port, username, password, databaseName, "utf8");
    }

    @Override
    public void login(String host, String port, String username, String password, String databaseName, String characterEncoding) throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?characterEncoding=" + characterEncoding;

        // Get a connection.
        this.connection = DriverManager.getConnection(url, username, password);
        this.statement = connection.createStatement();
    }

    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        return this.statement.executeQuery(query);

    }

    @Override
    public ResultSet executeSanitisedQuery(String query) throws SQLException {
        return this.executeQuery(query);
    }

    @Override
    public boolean reporterExists(Reporter reporter) {
        Reporter databaseReporter = getReporter(reporter.getCPR() + "");
        if (databaseReporter == null) {
            return false;
        }
        return databaseReporter.equals(reporter);
    }

    @Override
    public Reporter getReporter(String cpr) {
        String basicQuery = "Select cpr FROM Journalist WHERE cpr == " + cpr + ";";
        try {
            ResultSet resultSet = this.executeQuery(basicQuery);

            resultSet.beforeFirst();
            if (!resultSet.next()) {
                return null;
            }

            // Get the info of the reporter
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String streetName = resultSet.getString("streetName");
            int civicNumber = resultSet.getInt("civicNumber");
            int zipCode = resultSet.getInt("zipCode");
            String country = resultSet.getString("country");

            return new Reporter(Integer.parseInt(cpr), firstName, lastName, streetName, civicNumber, zipCode, country);
        } catch (SQLException e) {
            // ignored
        }
        return null;
    }

    @Override
    public void insertReporter(Reporter reporter) throws EntryExistsException {
        if (reporterExists(reporter)) {
            throw new EntryExistsException();
        }
        // TODO insert
    }

    @Override
    public boolean updateReporter(Reporter reporter) {
        if (reporterExists(reporter)) {
            // TODO update data
            return true;
        }
        else {
            try {
                insertReporter(reporter);
                return true;
            }
            catch (EntryExistsException e) {
                // Should never happen (already checked)
                return false;
            }
        }
    }

    @Override
    public boolean footageExists(Footage footage) {
        return false;
    }

    @Override
    public Footage getFootage(String footageTitle) {
        return null;
    }

    @Override
    public void insertFootage(Footage footage) throws EntryExistsException {

    }

    @Override
    public boolean updateFootage(Footage footage) {
        return false;
    }
}
