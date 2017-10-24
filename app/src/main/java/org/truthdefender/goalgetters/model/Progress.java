package org.truthdefender.goalgetters.model;

/**
 * Created by dj on 10/18/17.
 */

public class Progress {
    String date;
    String report;

    public Progress(String date, String report) {
        this.date = date;
        this.report = report;
    }

    public Progress() {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
