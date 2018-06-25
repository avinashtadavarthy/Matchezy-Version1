package com.example.yashwant.matchezy;

import org.json.JSONArray;

import java.util.ArrayList;

public class MatchedProfiles {

    private String Name;
    private String Category ;
    private String Description ;
    private String Thumbnail ;
    private String age;
    private JSONArray interests;
    private String user_id;

    public MatchedProfiles() {
    }

    public MatchedProfiles(String user_id, String name, String thumbnail,
                           String age, JSONArray interests) {
        Name = name;
        Thumbnail = thumbnail;
        this.age = age;
        this.interests = interests;
        this.user_id = user_id;
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

    public String getAge() {
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

    public void setAge(String age) {
        this.age = age;
    }

    public void setInterests(JSONArray array) {
        this.interests = interests;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
