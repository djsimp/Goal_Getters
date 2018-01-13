package org.truthdefender.goalgetters.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dj on 11/22/17.
 */

public class SmartGoal extends Goal {
    private String action;
    private String units;
    private float goal;
    private float progress;

    public SmartGoal(String action, String units, float goal, float progress, Date deadline, Date startdate, String group) {
        this.action = action;
        this.units = units;
        this.goal = goal;
        this.progress = progress;
        super.startdate = startdate;
        super.deadline = deadline;
        super.group = group;
        super.type = "smart";
    }

    public SmartGoal(String action, String units, float goal, float progress, Date deadline, Date startdate) {
        this.action = action;
        this.units = units;
        this.goal = goal;
        this.progress = progress;
        super.startdate = startdate;
        super.deadline = deadline;
        super.type = "smart";
    }

    public SmartGoal() {}

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        this.goal = goal;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getStopdate() {
        return stopdate;
    }

    public void setStopdate(Date stopdate) {
        this.stopdate = stopdate;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() { return super.type; }

    public void setType(String type) { super.type = type; }
}
