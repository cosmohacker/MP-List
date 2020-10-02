package com.asocialfingers.mp_list.Backend.Models.Category;

import java.io.Serializable;

public class CategoryDetails implements Serializable {

    String category_id, user_id, category_name, created_at;

    public CategoryDetails() {
    }

    public CategoryDetails(String category_id, String user_id, String category_name, String created_at) {
        this.category_id = category_id;
        this.user_id = user_id;
        this.category_name = category_name;
        this.created_at = created_at;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
