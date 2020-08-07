package com.example.android.homebakerapp.model;

public class Author {

    private String name;
    private String website;

    public Author() {};

    public Author(String name, String website) {

        this.name = name;
        this.website = website;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    };

}

