package org.truthdefender.goalgetters.model;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dj on 12/9/17.
 */

public class TaskGoalWrapper extends GoalWrapper {

    public TaskGoalWrapper(TaskGoal goal, List<Progress> progress_log) {
        super.goal = goal;
        super.progress_log = progress_log;
    }

    public TaskGoalWrapper(TaskGoal goal) {
        super.goal = goal;
        super.progress_log = new ArrayList<>();
    }

    @Override
    public String getTitle() {
        return ((TaskGoal)super.goal).getTaskTree().getTask();
    }

    @Override
    public float getPercentComplete() {
        if(super.goal != null && ((TaskGoal)super.goal).getTaskTree().getChildCount() != 0) {
            return ((TaskGoal)super.goal).getTaskTree().getCompletedCount() / ((TaskGoal)super.goal).getTaskTree().getChildCount();
        }
        return 0;
    }

    @Override
    public float getPercentLeft() {
        if(super.goal != null && ((TaskGoal)super.goal).getTaskTree().getChildCount() != 0) {
            return 1 - ((TaskGoal)super.goal).getTaskTree().getCompletedCount() / ((TaskGoal)super.goal).getTaskTree().getChildCount();
        }
        return 0;
    }

    @Override
    public String getStatus() {
        if(((TaskGoal)super.goal).getTaskTree().getCompletedCount() == ((TaskGoal)super.goal).getTaskTree().getChildCount()) {
            if(super.goal.getStopdate() == null) {
                super.goal.setStopdate(Calendar.getInstance().getTime());
            }
            return "Goal Achieved!";
        }
        float ontime = (float)Math.ceil(super.getPercentTimeTaken() * ((TaskGoal)super.goal).getTaskTree().getChildCount());
        StringBuilder sb = new StringBuilder();
        String unit = "tasks";
        if(Math.abs(ontime - ((TaskGoal)super.goal).getTaskTree().getCompletedCount()) == 1) {
            unit = "task";
        }
        if(ontime > ((TaskGoal)super.goal).getTaskTree().getCompletedCount()) {
            sb.append((int)(ontime - ((TaskGoal)super.goal).getTaskTree().getCompletedCount())).append(" ")
                    .append(unit).append(" ").append("behind");
        } else if(((TaskGoal)super.goal).getTaskTree().getCompletedCount() > ontime) {
            sb.append((int)(((TaskGoal)super.goal).getTaskTree().getCompletedCount() - ontime)).append(" ")
                    .append(unit).append(" ").append("ahead");
        } else {
            sb.append("On schedule");
        }
        return sb.toString();
    }

    @Override
    public String getFinalStatus() {
        if(((TaskGoal)super.goal).getTaskTree().getCompletedCount() == ((TaskGoal)super.goal).getTaskTree().getChildCount()) {
            if((super.goal).getStopdate() == null) {
                (super.goal).setStopdate(Calendar.getInstance().getTime());
            }
            return "Goal Achieved!";
        }
        return "You completed " + ((TaskGoal)super.goal).getTaskTree().getCompletedCount()
            + "/" + ((TaskGoal)super.goal).getTaskTree().getChildCount() + " tasks";
    }
}
