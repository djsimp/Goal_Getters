package org.truthdefender.goalgetters.model;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by dj on 10/17/17.
 */

public abstract class Goal {
    protected Date deadline;
    protected Date startdate;
    protected Date stopdate;
    protected String group;
    protected String goalId;
    protected String type;

    public abstract Date getDeadline();

    public abstract void setDeadline(Date deadline);

    public abstract Date getStartdate();

    public abstract void setStartdate(Date startdate);

    public abstract Date getStopdate();

    public abstract void setStopdate(Date stopdate);

    public abstract String getGroup();

    public abstract void setGroup(String group);

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
