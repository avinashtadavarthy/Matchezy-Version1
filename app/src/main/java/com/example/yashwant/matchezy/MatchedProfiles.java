package com.example.yashwant.matchezy;

import org.json.JSONArray;

import java.util.ArrayList;

public class MatchedProfiles {

    private String Name;
    private String Category ;
    private String Description ;
    private String Thumbnail ;
    private int age;
    private JSONArray interests;

    public MatchedProfiles() {
    }

    public MatchedProfiles(String name, String thumbnail,
                           int age, JSONArray interests) {
        Name = name;
        Thumbnail = thumbnail;
        this.age = age;
        this.interests = interests;
    }


    public String getName() {
        return Name;
    }

    public String getCategory() {
        return Category;
    }

    public String getDescription() {
        return Description;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public int getAge() {
        return age;
    }

    public JSONArray getInterests() {
        return interests;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setInterests(JSONArray array) {
        this.interests = interests;
    }

}
