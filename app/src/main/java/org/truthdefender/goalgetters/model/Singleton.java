package org.truthdefender.goalgetters.model;

import com.google.firebase.auth.FirebaseAuth;

import org.truthdefender.goalgetters.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dj on 10/21/17.
 */

public class Singleton {
    private static Singleton sSingleton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userId;
    private User user;
    private List<Goal> goals;
    private List<Group> groups;
    private List<Goal> past_goals;
    private Goal currentGoal;
    private Group currentGroup;

    private Singleton() {
        goals = new ArrayList<>();
        groups = new ArrayList<>();
        past_goals = new ArrayList<>();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public Goal getCurrentGoal() { return currentGoal; }

    public void setCurrentGoal(Goal goal) { currentGoal = goal; }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Group getCurrentGroup() { return currentGroup; }

    public void setCurrentGroup(Group group) { currentGroup = group; }

    public List<Goal> getPast_goals() {
        return past_goals;
    }

    public void setPast_goals(List<Goal> past_goals) {
        this.past_goals = past_goals;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.african, R.drawable.afro, R.drawable.asian, R.drawable.asian_1,
            R.drawable.avatar, R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3,
            R.drawable.bellboy, R.drawable.bellgirl, R.drawable.chicken, R.drawable.cooker,
            R.drawable.cooker_1, R.drawable.diver, R.drawable.diver_1, R.drawable.doctor,
            R.drawable.doctor_1, R.drawable.farmer, R.drawable.firefighter, R.drawable.firefighter_1,
            R.drawable.florist, R.drawable.florist_1, R.drawable.gentleman, R.drawable.hindu,
            R.drawable.hindu_1, R.drawable.hipster, R.drawable.horse, R.drawable.man,
            R.drawable.man_1, R.drawable.mechanic, R.drawable.mechanic_1, R.drawable.monk,
            R.drawable.musician, R.drawable.musician_1, R.drawable.muslim, R.drawable.muslim_1,
            R.drawable.nerd, R.drawable.nerd_1, R.drawable.ninja, R.drawable.nun,
            R.drawable.nurse, R.drawable.nurse_1, R.drawable.photographer, R.drawable.pilot,
            R.drawable.policeman, R.drawable.policewoman, R.drawable.priest, R.drawable.rapper,
            R.drawable.rapper_1, R.drawable.stewardess, R.drawable.surgeon, R.drawable.surgeon_1,
            R.drawable.telemarketer, R.drawable.telemarketer_1, R.drawable.waiter, R.drawable.waitress,
            R.drawable.woman, R.drawable.woman_1, R.drawable.woman_2
    };

    public Integer[] getThumbIds() {
        return mThumbIds;
    }
}
