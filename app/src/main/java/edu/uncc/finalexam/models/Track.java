package edu.uncc.finalexam.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Track implements Serializable {

    public Track(){}

    public int duration;

    public String id, title, link, preview, album_cover;

    public String getReadableDuration(){
        return String.format("%02d", duration / 60) + ":" + String.format("%02d", duration % 60);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return Objects.equals(id, track.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Track(JSONObject album, String album_cover) {
        this.album_cover = album_cover;
        try {
            this.id = String.valueOf(album.getInt("id"));
            this.duration = album.getInt("duration");
            this.title = album.getString("title");
            this.link = album.getString("link");
            this.preview = album.getString("preview");
        } catch (JSONException exc) {
            exc.printStackTrace();
        }
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbum_cover() {
        return album_cover;
    }

    public void setAlbum_cover(String album_cover) {
        this.album_cover = album_cover;
    }

    @Override
    public String toString() {
        return "Track{" +
                "duration=" + duration +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", preview='" + preview + '\'' +
                ", album_cover='" + album_cover + '\'' +
                '}';
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
}
