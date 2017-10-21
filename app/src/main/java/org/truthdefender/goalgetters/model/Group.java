package org.truthdefender.goalgetters.model;

import java.util.List;

/**
 * Created by dj on 10/21/17.
 */

public class Group {
    List<Person> members;
    String name;

    public Group(List<Person> members, String name) {
        this.members = members;
        this.name = name;
    }

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
