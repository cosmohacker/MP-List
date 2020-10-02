package com.asocialfingers.mp_list.Backend.Models.Model;

import java.io.Serializable;

public class ModelDetails implements Serializable {

    String model_id, user_id, category_id, brand_id, version_id, model_name, created_at;

    public ModelDetails() {
    }

    public ModelDetails(String model_id, String user_id, String category_id, String brand_id, String version_id, String model_name, String created_at) {
        this.model_id = model_id;
        this.user_id = user_id;
        this.category_id = category_id;
        this.brand_id = brand_id;
        this.version_id = version_id;
        this.model_name = model_name;
        this.created_at = created_at;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}


