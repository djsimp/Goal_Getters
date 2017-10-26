package org.truthdefender.goalgetters.model;

import java.util.HashMap;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dj on 10/19/17.
 */

public class User {

    private String uuid;
    private String name;
    private String email;
    private int profileImageTag;
    private HashMap<String,String> groups;
    private HashMap<String,String> goals;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, int profileImageTag, HashMap<String,String> groups, HashMap<String,String> goals) {
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
        this.groups = new HashMap<>();
        this.goals = new HashMap<>();
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

    public HashMap<String,String> getGroups() {
        return groups;
    }

    public void setGroups(HashMap<String,String> groups) {
        this.groups = groups;
    }

    public void addGroup(String groupid, String groupname) {
        groups.put(groupid, groupname);
    }

    public HashMap<String,String> getGoals() {
        return goals;
    }

    public void setGoals(HashMap<String,String> goals) {
        this.goals = goals;
    }

    public void addGoal(String goalid, String goalTitle) {
        goals.put(goalid, goalTitle);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}