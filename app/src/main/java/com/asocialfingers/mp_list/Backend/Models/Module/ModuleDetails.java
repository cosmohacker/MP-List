package com.asocialfingers.mp_list.Backend.Models.Module;

import java.io.Serializable;

public class ModuleDetails implements Serializable {

    String module_id, user_id, module_name, created_at;

    public ModuleDetails() {
    }

    public ModuleDetails(String module_id, String user_id, String module_name, String created_at) {
        this.module_id = module_id;
        this.user_id = user_id;
        this.module_name = module_name;
        this.created_at = created_at;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
