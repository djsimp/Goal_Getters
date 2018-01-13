package org.truthdefender.goalgetters.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Created by dj on 12/9/17.
 */

public class HabitGoalWrapper extends GoalWrapper {

    public HabitGoalWrapper(HabitGoal goal, List<Progress> progress_log) {
        super.goal = goal;
        super.progress_log = progress_log;
    }

    public HabitGoalWrapper(HabitGoal goal) {
        super.goal = goal;
        super.progress_log = new ArrayList<>();
    }

    @Override
    public String getTitle() {
        return ((HabitGoal)goal).getDescription();
    }

    @Override
    public float getPercentComplete() {
        int sum = 0;
        int total = 0;
        for(Integer index : ((HabitGoal)goal).getCalendarRatings().values()) {
            sum += index;
            total += 5;
        }
        return total != 0 ? sum / total : 0;
    }

    @Override
    public float getPercentLeft() {
        int sum = 0;
        int total = 0;
        for (Integer index : ((HabitGoal)goal).getCalendarRatings().values()) {
            sum += 5 - index;
            total += 5;
        }
        return total != 0 ? sum / total : 0;
    }

    @Override
    public float getTotalTime() {
        return 1.0f;
    }

    @Override
    public float getTimeTaken() {
        return ((HabitGoal)super.goal).getGoal();
    }

    @Override
    public float getTimeLeft() {
        return 1 - ((HabitGoal)super.goal).getGoal();
    }

    @Override
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        if(((HabitGoal)goal).getGoal() > getPercentComplete()) {
            sb.append(Math.round((((HabitGoal)goal).getGoal() - getPercentComplete())*100))
                .append("% under target!");
        } else if(((HabitGoal)goal).getGoal() < getPercentComplete()) {
            sb.append(Math.round((getPercentComplete() - ((HabitGoal)goal).getGoal())*100))
                .append("% over target.");
        } else {
            sb.append("on target!");
        }
        return sb.toString();
    }

    @Override
    public String getFinalStatus() {
        StringBuilder sb = new StringBuilder("You finished ");
        if(((HabitGoal)goal).getGoal() > getPercentComplete()) {
            sb.append(Math.round((((HabitGoal)goal).getGoal() - getPercentComplete())*100))
                    .append("% over your target!");
        } else if(((HabitGoal)goal).getGoal() < getPercentComplete()) {
            sb.append(Math.round((getPercentComplete() - ((HabitGoal)goal).getGoal())*100))
                    .append("% under your target.");
        } else {
            sb.append("on target!");
        }
        return sb.toString();
    }
}
