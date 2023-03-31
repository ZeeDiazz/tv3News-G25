package org.example;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Footage implements Queryable {
    private final String title;
    private final LocalDate shootingDate;
    private final Integer duration;

    public Footage(String title, LocalDate shootingDate, Integer duration) {
        this.title = title;
        this.shootingDate = shootingDate;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }
    public LocalDate getShootingDate() { return shootingDate; }
    public Integer getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        final String D = ";";
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

        return getTitle() + D + getShootingDate().toString() + D + getDuration();
    }

    @Override
    public String toQueryString() {
        return "('" + String.join("', '", new String[] {title, shootingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), duration.toString(), "10101010"}) + "')";
    }
}