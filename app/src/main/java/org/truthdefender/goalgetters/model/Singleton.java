package org.truthdefender.goalgetters.model;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by dj on 10/21/17.
 */

public class Singleton {
    private static Singleton sSingleton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User user;
    private List<Goal> goals;
    private List<Group> groups;
    private List<Goal> past_goals;

    private Singleton() {
    }

    public static Singleton get() {
        if(sSingleton == null) {
            sSingleton = new Singleton();
        }
        return sSingleton;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    public void setmAuthListener(FirebaseAuth.AuthStateListener mAuthListener) {
        this.mAuthListener = mAuthListener;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Goal> getPast_goals() {
        return past_goals;
    }

    public void setPast_goals(List<Goal> past_goals) {
        this.past_goals = past_goals;
    }
}
