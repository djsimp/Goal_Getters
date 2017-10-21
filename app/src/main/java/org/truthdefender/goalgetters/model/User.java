package org.truthdefender.goalgetters.model;

/**
 * Created by dj on 10/19/17.
 */

public class User {

    private String username;
    private String email;
    private String password;
    private String profileImageTag;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String password, String profileImageTag) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImageTag = profileImageTag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageTag() {
        return profileImageTag;
    }

    public void setProfileImageTag(String profileImageTag) {
        this.profileImageTag = profileImageTag;
    }
}