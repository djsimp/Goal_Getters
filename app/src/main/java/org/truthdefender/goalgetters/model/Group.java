package org.truthdefender.goalgetters.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dj on 10/21/17.
 */

public class Group {
    String name;
    String type;
    HashMap<String, String> members;
    HashMap<String, String> goals;
    String groupId;

    public Group(String name, String type, HashMap<String, String> members, HashMap<String, String> goals) {
        this.name = name;
        this.type = type;
        this.members = members;
        this.goals = goals;
    }

    public Group() {}

    public HashMap<String, String> getMembers() {
        if(members == null) {
            members = new HashMap<>();
        }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        StringBuilder sb = new StringBuilder();
        return sb.append(type).append(": ").append(name).toString();
    }

    public HashMap<String, String> getGoals() {
        if(goals == null) {
            goals = new HashMap<>();
        }
        return goals;
    }

    public void setGoals(HashMap<String, String> goals) {
        this.goals = goals;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
