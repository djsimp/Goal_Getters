package org.truthdefender.goalgetters.model;

import java.util.Date;

/**
 * Created by dj on 11/22/17.
 */

public class TaskGoal extends Goal {
    private TaskTree taskTree;

    public TaskGoal(TaskTree taskTree, Date deadline, Date startdate, String group) {
        this.taskTree = taskTree;
        super.startdate = startdate;
        super.deadline = deadline;
        super.group = group;
        super.type = "task";
    }

    public TaskGoal(TaskTree taskTree, Date deadline, Date startdate) {
        this.taskTree = taskTree;
        super.startdate = startdate;
        super.deadline = deadline;
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
        return super.deadline;
    }

    public void setDeadline(Date deadline) {
        super.deadline = deadline;
    }

    public Date getStartdate() {
        return super.startdate;
    }

    public void setStartdate(Date startdate) {
        super.startdate = startdate;
    }

    public Date getStopdate() {
        return super.stopdate;
    }

    public void setStopdate(Date stopdate) {
        super.stopdate = stopdate;
    }

    public String getGroup() {
        return super.group;
    }

    public void setGroup(String group) {
        super.group = group;
    }

    public String getType() { return super.type; }

    public void setType(String type) { super.type = type; }
}
