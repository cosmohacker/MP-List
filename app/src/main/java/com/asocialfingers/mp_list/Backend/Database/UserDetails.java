package com.asocialfingers.mp_list.Backend.Database;

import java.io.Serializable;

public class UserDetails implements Serializable {

    long user_id;
    String  username, password, permission, created_at;

    public UserDetails() {
    }

    public UserDetails(long user_id, String username, String password, String permission, String created_at) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.created_at = created_at;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
