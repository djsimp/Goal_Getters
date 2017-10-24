package org.truthdefender.goalgetters.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 10/19/17.
 */

public class User {

    private String name;
    private String email;
    private int profileImageTag;
    private List<Group> groups;
    private List<Goal> goals;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, int profileImageTag, List<Group> groups, List<Goal> goals) {
        this.name = name;
        this.email = email;
        this.profileImageTag = profileImageTag;
        this.groups = groups;
        this.goals = goals;
    }

    public User(String name, String email, int profileImageTag) {
        this.name = name;
        this.email = email;
        this.profileImageTag = profileImageTag;
        this.groups = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProfileImageTag() {
        return profileImageTag;
    }

    public void setProfileImageTag(int profileImageTag) {
        this.profileImageTag = profileImageTag;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }
}