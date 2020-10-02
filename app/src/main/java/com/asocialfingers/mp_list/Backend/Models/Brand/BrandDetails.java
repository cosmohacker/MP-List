package com.asocialfingers.mp_list.Backend.Models.Brand;

import java.io.Serializable;

public class BrandDetails implements Serializable {

    String brand_id, user_id, brand_name, created_at;

    public BrandDetails() {
    }

    public BrandDetails(String brand_id, String user_id, String brand_name, String created_at) {
        this.brand_id = brand_id;
        this.user_id = user_id;
        this.brand_name = brand_name;
        this.created_at = created_at;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
