package org.truthdefender.goalgetters.model;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dj on 10/18/17.
 */

public class Progress {
    String date;
    String report;
    String userId;
    int amount;

    public Progress(String date, String report, String userId, int amount) {
        this.date = date;
        this.report = report;
        this.userId = userId;
        this.amount = amount;
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

    public String getLog() {
        StringBuilder sb = new StringBuilder();
        sb.append(amount).append(" ")
                .append(" - ").append(report);
        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
