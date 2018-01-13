package org.truthdefender.goalgetters.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dj on 12/9/17.
 */

public class SmartGoalWrapper extends GoalWrapper {

    public SmartGoalWrapper(SmartGoal goal, List<Progress> progress_log) {
        super.goal = goal;
        super.progress_log = progress_log;
    }

    public SmartGoalWrapper(SmartGoal goal) {
        super.goal = goal;
        super.progress_log = new ArrayList<>();
    }

    public String getTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(((SmartGoal)super.goal).getAction()).append(" ")
                .append((int)((SmartGoal)super.goal).getGoal()).append(" ");
        if(((SmartGoal)super.goal).getGoal() == 1) {
            sb.append(((SmartGoal)super.goal).getUnits()
                    .substring(0, ((SmartGoal)super.goal).getUnits().length() - 1));
        } else {
            sb.append(((SmartGoal)super.goal).getUnits());
        }
        return sb.toString();
    }

    public float getPercentComplete() { return ((SmartGoal)super.goal).getProgress() / ((SmartGoal)super.goal).getGoal(); }

    public float getPercentLeft() { return 1 - getPercentComplete(); }

    public String getStatus() {
        if(((SmartGoal)super.goal).getProgress() >= ((SmartGoal)super.goal).getGoal()) {
            if(((SmartGoal)super.goal).getStopdate() == null) {
                ((SmartGoal)super.goal).setStopdate(Calendar.getInstance().getTime());
            }
            return "Goal Achieved!";
        }
        float ontime = (float)Math.ceil(super.getPercentTimeTaken() * ((SmartGoal)super.goal).getGoal());
        StringBuilder sb = new StringBuilder();
        String curUnits = ((SmartGoal)super.goal).getUnits();
        if(Math.abs(ontime - ((SmartGoal)super.goal).getProgress()) == 1
                && ((SmartGoal)super.goal).getUnits().charAt(((SmartGoal)super.goal).getUnits().length() - 1) == 's') {
            curUnits = ((SmartGoal)super.goal).getUnits().substring(0, ((SmartGoal)super.goal).getUnits().length() - 1);
        }
        if(ontime > ((SmartGoal)super.goal).getProgress()) {
            sb.append((int)(ontime - ((SmartGoal)super.goal).getProgress())).append(" ")
                    .append(curUnits).append(" ").append("behind");
        } else if(((SmartGoal)super.goal).getProgress() > ontime) {
            sb.append((int)(((SmartGoal)super.goal).getProgress() - ontime)).append(" ")
                    .append(curUnits).append(" ").append("ahead");
        } else {
            sb.append("On schedule");
        }
        return sb.toString();
    }

    public String getFinalStatus() {
        if(((SmartGoal)super.goal).getProgress() >= ((SmartGoal)super.goal).getGoal()) {
            if(((SmartGoal)super.goal).getStopdate() == null) {
                ((SmartGoal)super.goal).setStopdate(Calendar.getInstance().getTime());
            }
            return "Goal Achieved!";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("You ").append(((SmartGoal)super.goal).getAction().toLowerCase()).append(" ")
                .append((int)((SmartGoal)super.goal).getProgress()).append(" ").append(((SmartGoal)super.goal).getUnits().toLowerCase());
        return sb.toString();
    }
}
