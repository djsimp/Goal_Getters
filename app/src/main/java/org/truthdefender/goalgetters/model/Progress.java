package org.truthdefender.goalgetters.model;

/**
 * Created by dj on 10/18/17.
 */

public class Progress {
    String date;
    String report;
    String userId;
    String name;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
