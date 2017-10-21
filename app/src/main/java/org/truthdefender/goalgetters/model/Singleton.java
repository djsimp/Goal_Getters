package org.truthdefender.goalgetters.model;

import java.util.List;

/**
 * Created by dj on 10/21/17.
 */

public class Singleton {
    private static Singleton sSingleton;
    User user;
    List<Goal> goals;
    List<Group> groups;
    List<Goal> past_goals;

    public static Singleton get() {
        if(sSingleton == null) {
            sSingleton = new Singleton();
        }
        return sSingleton;
    }
}
