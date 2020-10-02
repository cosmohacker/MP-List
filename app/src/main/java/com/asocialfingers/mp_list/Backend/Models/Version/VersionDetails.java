package com.asocialfingers.mp_list.Backend.Models.Version;

public class VersionDetails {

    String version_id, user_id, brand_id, module_id, model_id, category_id, version_name, created_at;

    public VersionDetails() {
    }

    public VersionDetails(String version_id, String user_id, String brand_id, String module_id, String model_id, String category_id, String version_name, String created_at) {
        this.version_id = version_id;
        this.user_id = user_id;
        this.brand_id = brand_id;
        this.module_id = module_id;
        this.model_id = model_id;
        this.category_id = category_id;
        this.version_name = version_name;
        this.created_at = created_at;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
