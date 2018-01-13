package org.truthdefender.goalgetters.model;

import java.util.Date;

/**
 * Created by dj on 11/22/17.
 */

public class TaskGoal extends Goal {
    private TaskTree taskTree;

    public TaskGoal(TaskTree taskTree) {
        this.taskTree = taskTree;
        super.type = "task";
    }

    public TaskGoal() {
        this.taskTree = new TaskTree();
        super.type = "task";
    }

    public TaskTree getTaskTree() {
        return taskTree;
    }

    public void setTaskTree(TaskTree taskTree) {
        this.taskTree = taskTree;
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
