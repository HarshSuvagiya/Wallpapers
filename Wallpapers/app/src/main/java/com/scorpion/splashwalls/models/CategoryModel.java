package com.scorpion.splashwalls.models;

public class CategoryModel {

    String link;
    String name;

    public CategoryModel() {
    }

    public CategoryModel(String link, String name) {
        this.link = link;
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
