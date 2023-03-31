package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Database {
    void login(String host, String port, String username, String password, String databaseName) throws SQLException;
    void login(String host, String port, String username, String password, String databaseName, String characterEncoding) throws SQLException;

    ResultSet executeQuery(String query) throws SQLException;
    ResultSet executeSanitisedQuery(String query) throws SQLException;
    void executeUpdate(String sql) throws SQLException;
    void executeSanitisedUpdate(String sql) throws SQLException;

    boolean reporterExists(String reporterCpr);
    boolean reporterExists(Reporter reporter);
    Reporter getReporter(String cpr);
    void insertReporter(Reporter reporter) throws EntryExistsException;
    boolean updateReporter(Reporter reporter);

    boolean footageExists(String footageTitle);
    boolean footageExists(Footage footage);
    Footage getFootage(String footageTitle);
    void insertFootage(Footage footage) throws EntryExistsException;
    boolean updateFootage(Footage footage);
}
