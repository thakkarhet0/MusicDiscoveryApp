package edu.uncc.finalexam.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Album implements Serializable {

    public int id, nb_tracks, genre_id;
    public String title, link, cover, cover_small, cover_medium, cover_big, tracklist, type;
    public Artist artist;

    public Album(){}

    public Album(JSONObject album) {
        try {
            this.id = album.getInt("id");
            this.nb_tracks = album.getInt("nb_tracks");
            this.genre_id = album.getInt("genre_id");
            this.title = album.getString("title");
            this.link = album.getString("link");
            this.cover = album.getString("cover");
            this.cover_big = album.getString("cover_big");
            this.cover_medium = album.getString("cover_medium");
            this.cover_small = album.getString("cover_small");
            this.tracklist = album.getString("tracklist");
            this.type = album.getString("type");
            this.artist = new Artist(album.getJSONObject("artist"));
        } catch (JSONException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", nb_tracks=" + nb_tracks +
                ", genre_id=" + genre_id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", cover='" + cover + '\'' +
                ", cover_small='" + cover_small + '\'' +
                ", cover_medium='" + cover_medium + '\'' +
                ", cover_big='" + cover_big + '\'' +
                ", tracklist='" + tracklist + '\'' +
                ", type='" + type + '\'' +
                ", artist=" + artist +
                '}';
    }

    public String getCover_medium() {
        return cover_medium;
    }

    public void setCover_medium(String cover_medium) {
        this.cover_medium = cover_medium;
    }

    public String getCover_big() {
        return cover_big;
    }

    public void setCover_big(String cover_big) {
        this.cover_big = cover_big;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getCover_small() {
        return cover_small;
    }

    public void setCover_small(String cover_small) {
        this.cover_small = cover_small;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNb_tracks() {
        return nb_tracks;
    }

    public void setNb_tracks(int nb_tracks) {
        this.nb_tracks = nb_tracks;
    }

    public int getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTracklist() {
        return tracklist;
    }

    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
