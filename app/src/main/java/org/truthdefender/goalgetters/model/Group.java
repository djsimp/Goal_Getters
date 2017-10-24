package org.truthdefender.goalgetters.model;

import java.util.List;

/**
 * Created by dj on 10/21/17.
 */

public class Group {
    String name;
    List<Person> members;
    List<Goal> goals;

    public Group(String name, List<Person> members, List<Goal> goals) {
        this.name = name;
        this.members = members;
        this.goals = goals;
    }

    public Group() {}

    public List<Person> getMembers() {
        return members;
    }

    public void setMembers(List<Person> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
