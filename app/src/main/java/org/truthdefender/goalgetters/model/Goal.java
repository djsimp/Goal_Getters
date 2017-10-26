package org.truthdefender.goalgetters.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dj on 10/17/17.
 */

public class Goal {
    private String title;
    private String units;
    private int goal;
    private int progress;
//    private int startYear, startMonth, startDate, startHour, startMinute, startSecond;
//    private int endYear, endMonth, endDate, endHour, endMinute, endSecond;
    private Date deadline;
    private Date startdate;
    private String group;
    private List<Progress> progress_log;

    public Goal(String title, String units, int goal, int progress, Date deadline, Date startdate, String group) {
        this.title = title;
        this.units = units;
        this.goal = goal;
        this.progress = progress;
        this.deadline = deadline;
        this.startdate = startdate;
//        this.endYear = deadline.get(Calendar.YEAR);
//        this.endMonth = deadline.get(Calendar.MONTH);
//        this.endDate = deadline.get(Calendar.DATE);
//        this.endHour = deadline.get(Calendar.HOUR);
//        this.endMinute = deadline.get(Calendar.MINUTE);
//        this.endSecond = deadline.get(Calendar.SECOND);
//        this.startYear = startdate.get(Calendar.YEAR);
//        this.startMonth = startdate.get(Calendar.MONTH);
//        this.startDate = startdate.get(Calendar.DATE);
//        this.startHour = startdate.get(Calendar.HOUR);
//        this.startMinute = startdate.get(Calendar.MINUTE);
//        this.startSecond = startdate.get(Calendar.SECOND);
        this.group = group;
        this.progress_log = new ArrayList<>();
    }

    public Goal(String title, String units, int goal, int progress, Date deadline, Date startdate) {
        this.title = title;
        this.units = units;
        this.goal = goal;
        this.progress = progress;
        this.deadline = deadline;
        this.startdate = startdate;
//        this.endYear = deadline.get(Calendar.YEAR);
//        this.endMonth = deadline.get(Calendar.MONTH);
//        this.endDate = deadline.get(Calendar.DATE);
//        this.endHour = deadline.get(Calendar.HOUR);
//        this.endMinute = deadline.get(Calendar.MINUTE);
//        this.endSecond = deadline.get(Calendar.SECOND);
//        this.startYear = startdate.get(Calendar.YEAR);
//        this.startMonth = startdate.get(Calendar.MONTH);
//        this.startDate = startdate.get(Calendar.DATE);
//        this.startHour = startdate.get(Calendar.HOUR);
//        this.startMinute = startdate.get(Calendar.MINUTE);
//        this.startSecond = startdate.get(Calendar.SECOND);
        this.progress_log = new ArrayList<>();
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

    public float getPercentComplete() { return (float)(progress / goal); }

    public float getPercentLeft() { return (float)(1 - progress / goal); }

    public Date getDeadline() {
//        Calendar deadline = Calendar.getInstance();
//        deadline.set(Calendar.YEAR, endYear);
//        deadline.set(Calendar.MONTH, endMonth);
//        deadline.set(Calendar.DATE, endDate);
//        deadline.set(Calendar.HOUR, endHour);
//        deadline.set(Calendar.MINUTE, endMinute);
//        deadline.set(Calendar.SECOND, endSecond);
        return deadline;
    }

    public void setDeadline(Date deadline) {
//        this.endYear = deadline.get(Calendar.YEAR);
//        this.endMonth = deadline.get(Calendar.MONTH);
//        this.endDate = deadline.get(Calendar.DATE);
//        this.endHour = deadline.get(Calendar.HOUR);
//        this.endMinute = deadline.get(Calendar.MINUTE);
//        this.endSecond = deadline.get(Calendar.SECOND);
        this.deadline = deadline;
    }

    public Date getStartdate() {
//        Calendar startdate = Calendar.getInstance();
//        startdate.set(Calendar.YEAR, startYear);
//        startdate.set(Calendar.MONTH, startMonth);
//        startdate.set(Calendar.DATE, startDate);
//        startdate.set(Calendar.HOUR, startHour);
//        startdate.set(Calendar.MINUTE, startMinute);
//        startdate.set(Calendar.SECOND, startSecond);
        return startdate;
    }

    public void setStartdate(Date startdate) {
//        this.startYear = startdate.get(Calendar.YEAR);
//        this.startMonth = startdate.get(Calendar.MONTH);
//        this.startDate = startdate.get(Calendar.DATE);
//        this.startHour = startdate.get(Calendar.HOUR);
//        this.startMinute = startdate.get(Calendar.MINUTE);
//        this.startSecond = startdate.get(Calendar.SECOND);
        this.startdate = startdate;
    }

    public long getTotalTime() {
        Calendar start = Calendar.getInstance();
        start.setTime(startdate);
        Calendar end = Calendar.getInstance();
        end.setTime(deadline);
        return end.getTimeInMillis() - start.getTimeInMillis();
    }

    public long getTimeTaken() {
        Calendar start = Calendar.getInstance();
        start.setTime(startdate);
        return Calendar.getInstance().getTimeInMillis() - start.getTimeInMillis();
    }

    public long getTimeLeft() {
        Calendar end = Calendar.getInstance();
        end.setTime(deadline);
        return end.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
    }

    public float getPercentTimeTaken() { return (float)(getTimeTaken() / getTotalTime()); }

    public float getPercentTimeLeft() { return (float)(1 - getTimeTaken() / getTotalTime()); }

    public String getStatus() {
        int ontime = Math.round(getPercentTimeTaken() * goal);
        StringBuilder sb = new StringBuilder();
        String curUnits = units;
        if(Math.abs(ontime - progress) == 1 && units.charAt(units.length()-1) == 's') {
            curUnits = units.substring(0, units.length()-1);
        }
        if(ontime > progress) {
            sb.append(ontime - progress).append(" ").append(curUnits).append(" ").append("behind");
        } else if(progress > ontime) {
            sb.append(progress - ontime).append(" ").append(curUnits).append(" ").append("ahead");
        } else {
            sb.append("You are on schedule");
        }
        return sb.toString();
    }

    public String getDaysLeft() {
        double daysleft = TimeUnit.DAYS.convert(getTimeLeft(), TimeUnit.MILLISECONDS);
        StringBuilder sb = new StringBuilder();
        if(daysleft > 1) {
            Double myDouble = Double.valueOf(daysleft);
            Integer days = Integer.valueOf(myDouble.intValue());
            sb.append(days);
            if(days == 1) {
                sb.append(" day left");
            } else {
                sb.append(" days left");
            }
        } else {
            daysleft = TimeUnit.HOURS.convert(getTimeLeft(), TimeUnit.MILLISECONDS);
            Double myDouble = Double.valueOf(daysleft);
            Integer hours = Integer.valueOf(myDouble.intValue());
            sb.append(hours);
            if(hours == 1) {
                sb.append(" hour left");
            } else {
                sb.append(" hours left");
            }
        }
        return sb.toString();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
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
