package com.asocialfingers.mp_list.Backend.Models.Users;

import java.io.Serializable;

public class UsersDetails implements Serializable {

    String id, username, password, permission, timestamp;

    public UsersDetails() {
    }

    public UsersDetails(String id, String username, String password, String permission, String timestamp) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
