package org.truthdefender.goalgetters.model;


import java.util.Date;
import java.util.HashMap;

/**
 * Created by dj on 11/22/17.
 */

public class HabitGoal extends Goal {
    private String description;
    private HashMap<Date, Integer> calendarRatings;
    private String frequencyType;
    private HashMap<String, Boolean> frequencyMap;
    private float goal;

    public HabitGoal(String description, String frequencyType, HashMap<String, Boolean> frequencyMap, float goal, Date deadline, Date startdate, String group) {
        this.description = description;
        this.calendarRatings = new HashMap<>();
        this.frequencyType = frequencyType;
        this.frequencyMap = frequencyMap;
        this.goal = goal;
        super.startdate = startdate;
        super.deadline = deadline;
        super.group = group;
        super.type = "habit";
    }

    public HabitGoal(String description, String frequencyType, HashMap<String, Boolean> frequencyMap, float goal, Date deadline, Date startdate) {
        this.description = description;
        this.calendarRatings = new HashMap<>();
        this.frequencyType = frequencyType;
        this.frequencyMap = frequencyMap;
        this.goal = goal;
        super.startdate = startdate;
        super.deadline = deadline;
        super.type = "habit";
    }

    public HabitGoal() {
        this.calendarRatings = new HashMap<>();
        this.frequencyMap = new HashMap<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<Date, Integer> getCalendarRatings() {
        return calendarRatings;
    }

    public void setCalendarRatings(HashMap<Date, Integer> calendarRatings) {
        this.calendarRatings = calendarRatings;
    }

    public void putCalendarRating(Date date, int rating) {
        calendarRatings.put(date, rating);
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public HashMap<String, Boolean> getFrequencyMap() {
        return frequencyMap;
    }

    public void setFrequencyMap(HashMap<String, Boolean> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        this.goal = goal;
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
