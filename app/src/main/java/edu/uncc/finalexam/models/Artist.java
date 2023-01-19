package edu.uncc.finalexam.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Artist implements Serializable {

    public int id;
    public String name, link, picture, picture_small, type;

    public Artist(JSONObject artist) {
        try {
            this.id = artist.getInt("id");
            this.name = artist.getString("name");
            this.link = artist.getString("link");
            this.picture = artist.getString("picture");
            this.picture_small = artist.getString("picture_small");
            this.type = artist.getString("type");
        } catch (JSONException exc) {
            exc.printStackTrace();
        }
    }

    public String getPicture_small() {
        return picture_small;
    }

    public void setPicture_small(String picture_small) {
        this.picture_small = picture_small;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", picture='" + picture + '\'' +
                ", picture_small='" + picture_small + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
