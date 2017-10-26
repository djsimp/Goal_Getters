package org.truthdefender.goalgetters.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dj on 10/21/17.
 */

public class Group {
    String name;
    HashMap<String, String> members;
    HashMap<String, Boolean> goals;

    public Group(String name, HashMap<String, String> members, HashMap<String, Boolean> goals) {
        this.name = name;
        this.members = members;
        this.goals = goals;
    }

    public Group() {}

    public HashMap<String, String> getMembers() {
        return members;
    }

    public String getMemberList() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> member : members.entrySet()) {
            sb.append(member.getValue()).append(", ");
        }
        sb.delete(sb.length()-2, sb.length()-1);
        return sb.toString();
    }

    public void setMembers(HashMap<String, String> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Boolean> getGoals() {
        return goals;
    }

    public void setGoals(HashMap<String, Boolean> goals) {
        this.goals = goals;
    }
}
