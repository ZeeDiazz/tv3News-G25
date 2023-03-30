package org.example;

public interface Database {
    boolean reporterExists(Reporter reporter);
    Reporter getReporter(String cpr);
    void insertReporter(Reporter reporter) throws EntryExistsException;
    boolean updateReporter(Reporter reporter);

    boolean footageExists(Footage footage);
    Footage getFootage(String footageTitle);
    void insertFootage(Footage footage) throws EntryExistsException;
    boolean updateFootage(Footage footage);
}
