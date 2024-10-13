package com.iud.api_fetching.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String price;
    private String rating;
    private String description;
    private String img;
    private String id;

    public Product(String name, String price, String rating, String description, String img, String id) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.img = img;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
