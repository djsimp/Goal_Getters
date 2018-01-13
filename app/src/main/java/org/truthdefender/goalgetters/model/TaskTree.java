package org.truthdefender.goalgetters.model;

import java.util.ArrayList;

/**
 * Created by dj on 1/1/18.
 */

public class TaskTree {
    private String task;
    private ArrayList<TaskTree> children;
    private boolean completed;

    public TaskTree(String task, ArrayList<TaskTree> children) {
        this.task = task;
        this.children = children;
        this.completed = false;
    }

    public TaskTree(String task) {
        this.task = task;
        this.children = new ArrayList<>();
        this.completed = false;
    }

    public TaskTree() {
        this.children = new ArrayList<>();
        this.completed = false;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public ArrayList<TaskTree> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TaskTree> children) {
        this.children = children;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markComplete() {
        this.completed = true;
        for(TaskTree curTask : this.children) {
            curTask.markComplete();
        }
    }

    public void addChild(TaskTree taskTree) {
        this.children.add(taskTree);
    }

    public void deleteChild(String task) {
        for(TaskTree curTask : this.children) {
            if(curTask.getTask() != null && curTask.getTask().equals(task)) {
                this.children.remove(curTask);
            }
        }
    }

    public int getCompletedCount() {
        int completed = 0;
        for(TaskTree curTask : this.children) {
            if(curTask.isCompleted()) {
                completed++;
            }
        }
        return completed;
    }

    public int getChildCount() {
        return this.children.size();
    }
}
