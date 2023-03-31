package org.example;

import java.beans.PropertyEditorSupport;
import java.security.KeyPair;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;


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
    }

    @Override
    public void closeStatement() throws SQLException {
        if (statement == null) {
            return;
        }
        try {
            while (!statement.isClosed()) {
                statement.close();
                Thread.sleep(10);
            }
        }
        catch (InterruptedException e) {
            // ignored
        }
    }

    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        closeStatement();

        statement = connection.createStatement();
        statement.closeOnCompletion();
        return this.statement.executeQuery(query);
    }

    @Override
    public ResultSet executeSanitisedQuery(String query) throws SQLException {
        return this.executeQuery(query);
    }

    @Override
    public void executeUpdate(String sql) throws SQLException {
        closeStatement();

        statement = connection.createStatement();
        statement.closeOnCompletion();
        this.statement.executeUpdate(sql);
    }

    @Override
    public void executeSanitisedUpdate(String sql) throws SQLException {
        executeUpdate(sql);
    }

    protected ResultSet selectStatement(String[] whatToSelect, String from, String condition) throws SQLException {
        String query = "SELECT " + String.join(", ", whatToSelect) + " FROM " + from + " WHERE " + condition;
        return this.executeQuery(query);
    }

    protected ResultSet selectStatement(String whatToSelect, String from, String condition) throws SQLException {
        return selectStatement(new String[] {whatToSelect}, from, condition);
    }

    protected void insertStatement(String where, Queryable queryable) throws SQLException {
        String query = "INSERT INTO " + where + " VALUES " + queryable.toQueryString();
        executeUpdate(query);
    }

    protected void updateStatement(String where, HashMap<String, String> what, String condition) throws SQLException {
        ArrayList<String> whatToSet = new ArrayList<>();
        for (Map.Entry<String, String> entry : what.entrySet()) {
            String temp = entry.getKey() + " = " + entry.getValue();
            whatToSet.add(temp);
        }

        String query = "UPDATE " + where + " SET " + String.join(", ", whatToSet) + " WHERE " + condition;
        executeUpdate(query);
    }

    @Override
    public boolean reporterExists(String reporterCpr) {
        return getReporter(reporterCpr) != null;
    }

    @Override
    public boolean reporterExists(Reporter reporter) {
        if (reporter == null) {
            return false;
        }

        Reporter databaseReporter = getReporter(reporter.getCPR() + "");
        return reporter.equals(databaseReporter);
    }

    @Override
    public Reporter getReporter(String cpr) {
        try {
            ResultSet resultSet = this.selectStatement("cpr", "Journalist", "cpr = " + cpr);

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
        try {
            insertStatement("Journalist", reporter);
        }
        catch (SQLException e) {
            // TODO how to handle?
        }
    }

    @Override
    public boolean updateReporter(Reporter reporter) {
        if (reporterExists(reporter)) {
            HashMap<String, String> data = new HashMap<>();
            data.put("firstName", reporter.getFirstName());
            data.put("lastName", reporter.getLastName());
            data.put("streetName", reporter.getStreetName());
            data.put("civicNumber", reporter.getCivicNumber().toString());
            data.put("zipCode", reporter.getZIPCode().toString());
            data.put("country", reporter.getCountry());

            try {
                updateStatement("Journalist", data, "cpr = " + reporter.getCPR());
                return true;
            }
            catch (SQLException e) {
                return false;
            }
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
    public boolean footageExists(String footageTitle) {
        return getFootage(footageTitle) != null;
    }

    @Override
    public boolean footageExists(Footage footage) {
        if (footage == null) {
            return false;
        }

        Footage databaseFootage = getFootage(footage.getTitle());
        return footage.equals(databaseFootage);
    }

    @Override
    public Footage getFootage(String footageTitle) {
        try {
            ResultSet resultSet = this.selectStatement("footageTitle", "Footage", "footageTitle = " + footageTitle);

            resultSet.beforeFirst();
            if (!resultSet.next()) {
                return null;
            }

            // Get the info of the reporter
            LocalDate shootingDate = resultSet.getDate("shootingDate").toLocalDate();
            int duration = resultSet.getInt("secDuration");
            String reporterCpr = resultSet.getString("cpr");

            return new Footage(footageTitle, shootingDate, duration);
        } catch (SQLException e) {
            // ignored
        }
        return null;
    }

    @Override
    public void insertFootage(Footage footage) throws EntryExistsException {
        if (footageExists(footage)) {
            throw new EntryExistsException();
        }
        try {
            this.insertStatement("footage", footage);
        }
        catch (SQLException e) {
            // TODO how to handle?
        }
    }

    @Override
    public boolean updateFootage(Footage footage) {
        if (footageExists(footage)) {
            HashMap<String, String> data = new HashMap<>();
            data.put("shootingDate", footage.getShootingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            data.put("secDuration", footage.getDuration().toString());

            try {
                updateStatement("Footage", data, "footageTitle = " + footage.getTitle());
                return true;
            }
            catch (SQLException e) {
                return false;
            }
        }
        else {
            try {
                insertFootage(footage);
                return true;
            }
            catch (EntryExistsException e) {
                // Should never happen (already checked)
                return false;
            }
        }
    }
}
