package org.example;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Footage {
    private final String title;
    private final LocalDate date;
    private final Integer duration;

    public Footage(String title, LocalDate date, Integer duration) {
        this.title = title;
        this.date = date;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }
    public LocalDate getDate() { return date; }
    public Integer getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        final String D = ";";
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

        return getTitle() + D + getDate().toString() + D + getDuration();
    }

}