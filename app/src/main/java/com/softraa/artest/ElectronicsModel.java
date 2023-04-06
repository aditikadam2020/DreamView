package com.softraa.artest;

import java.io.Serializable;

public class ElectronicsModel implements Serializable {
    private String id;
    private String title;
    private String resource;
    private String description;
    private String image;
    private String price;
    private boolean show;

    public ElectronicsModel() {
    }

    public ElectronicsModel(String id, String title, String resource, String description, String image, String price, boolean show) {
        this.id = id;
        this.title = title;
        this.resource = resource;
        this.description = description;
        this.image = image;
        this.price = price;
        this.show = show;
    }

    @Override
    public String toString() {
        return "ElectronicsModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", resource='" + resource + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                ", show=" + show +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
