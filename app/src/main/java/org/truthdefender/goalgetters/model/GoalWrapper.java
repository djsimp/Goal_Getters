package org.truthdefender.goalgetters.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dj on 12/9/17.
 */

public abstract class GoalWrapper {
    protected Goal goal;
    protected List<Progress> progress_log;


    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public List<Progress> getProgressLog() {
        if(progress_log == null) {
            progress_log = new ArrayList<Progress>();
        }
        return progress_log;
    }

    public void setProgressLog(List<Progress> progress_log) {
        if(this.progress_log == null) {
            this.progress_log = new ArrayList<>();
        }
        this.progress_log = progress_log;
    }

    public void addProgressToLog(Progress progress) {
        if(this.progress_log == null) {
            this.progress_log = new ArrayList<>();
        }
        this.progress_log.add(progress);
    }

    public abstract String getTitle();

    public float getTotalTime() {
        Calendar start = Calendar.getInstance();
        start.setTime(goal.getStartdate());
        Calendar end = Calendar.getInstance();
        end.setTime(goal.getDeadline());
        return (float)(end.getTimeInMillis() - start.getTimeInMillis());
    }

    public float getTimeTaken() {
        Calendar start = Calendar.getInstance();
        start.setTime(goal.getStartdate());
        if(goal.getStopdate() != null) {
            Calendar end = Calendar.getInstance();
            end.setTime(goal.getStopdate());
            return (float)(end.getTimeInMillis() - start.getTimeInMillis());
        } else {
            return (float)(Calendar.getInstance().getTimeInMillis() - start.getTimeInMillis());
        }
    }

    public float getTimeLeft() {
        Calendar end = Calendar.getInstance();
        end.setTime(goal.getDeadline());
        if(goal.getStopdate() != null) {
            Calendar stop = Calendar.getInstance();
            stop.setTime(goal.getStopdate());
            return (float)(end.getTimeInMillis() - stop.getTimeInMillis());
        } else {
            return (float)(end.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        }
    }

    public float getPercentTimeTaken() {
        float time = (getTimeTaken() / getTotalTime());
        if(time > 1) {
            return 1.0f;
        } else if(time < 0) {
            return 0.0f;
        } else {
            return time;
        }
    }

    public float getPercentTimeLeft() {
        float time = (1 - getTimeTaken() / getTotalTime());
        if(time > 1) {
            return 1.0f;
        } else if(time < 0) {
            return 0.0f;
        } else {
            return time;
        }
    }

    public abstract float getPercentComplete();

    public abstract float getPercentLeft();

    public abstract String getStatus();

    public abstract String getFinalStatus();

    public String getDaysLeft() {
        double daysleft = TimeUnit.DAYS.convert((long)getTimeLeft(), TimeUnit.MILLISECONDS);
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
            daysleft = TimeUnit.HOURS.convert((long)getTimeLeft(), TimeUnit.MILLISECONDS);
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
}
