package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tv3Database implements Database {


    @Override
    public void login(String host, String port, String username, String password, String databaseName) throws SQLException {

    }

    @Override
    public void login(String host, String port, String username, String password, String databaseName, String characterEncoding) throws SQLException {

    }

    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        return null;
    }

    @Override
    public ResultSet executeSanitisedQuery(String query) throws SQLException {
        return null;
    }

    @Override
    public boolean reporterExists(Reporter reporter) {
        return false;
    }

    @Override
    public Reporter getReporter(String cpr) {
        return null;
    }

    @Override
    public void insertReporter(Reporter reporter) throws EntryExistsException {

    }

    @Override
    public boolean updateReporter(Reporter reporter) {
        return false;
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
