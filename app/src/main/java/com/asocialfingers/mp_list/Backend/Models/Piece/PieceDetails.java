package com.asocialfingers.mp_list.Backend.Models.Piece;

import java.io.Serializable;

public class PieceDetails implements Serializable {

    String piece_id, user_id, brand_id, category_id, model_id, module_id, version_id, price, currency, count, piece_name, junk_count, description, note, image, created_at;

    public PieceDetails() {
    }

    public PieceDetails(String piece_id, String user_id, String brand_id, String category_id, String model_id, String module_id, String version_id, String price, String currency, String count, String piece_name, String junk_count, String description, String note, String image, String created_at) {
        this.piece_id = piece_id;
        this.user_id = user_id;
        this.brand_id = brand_id;
        this.category_id = category_id;
        this.model_id = model_id;
        this.module_id = module_id;
        this.version_id = version_id;
        this.price = price;
        this.currency = currency;
        this.count = count;
        this.piece_name = piece_name;
        this.junk_count = junk_count;
        this.description = description;
        this.note = note;
        this.image = image;
        this.created_at = created_at;
    }

    public String getPiece_id() {
        return piece_id;
    }

    public void setPiece_id(String piece_id) {
        this.piece_id = piece_id;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPiece_name() {
        return piece_name;
    }

    public void setPiece_name(String piece_name) {
        this.piece_name = piece_name;
    }

    public String getJunk_count() {
        return junk_count;
    }

    public void setJunk_count(String junk_count) {
        this.junk_count = junk_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
