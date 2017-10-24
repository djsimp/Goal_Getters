package org.truthdefender.goalgetters.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dj on 10/17/17.
 */

public class Goal {
    private String title;
    private String units;
    private int goal;
    private int progress;
    private Calendar deadline;
    private Calendar startdate;
    private Group group;
    private List<Progress> progress_log;

    public Goal(String title, String units, int goal, int progress, Calendar deadline, Calendar startdate, Group group) {
        this.title = title;
        this.units = units;
        this.goal = goal;
        this.progress = progress;
        this.deadline = deadline;
        this.startdate = startdate;
        this.group = group;
    }

    public Goal() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public Calendar getStartdate() {
        return startdate;
    }

    public void setStartdate(Calendar startdate) {
        this.startdate = startdate;
    }

    public long getTotalTime() { return deadline.getTimeInMillis() - startdate.getTimeInMillis(); }

    public long getTimeTaken() { return Calendar.getInstance().getTimeInMillis() - startdate.getTimeInMillis(); }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Progress> getProgressLog() {
        return progress_log;
    }

    public void setProgressLog(List<Progress> progress_log) {
        this.progress_log = progress_log;
    }

    public void addProgressToLog(Progress progress) {
        this.progress_log.add(progress);
    }
}
