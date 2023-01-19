package edu.uncc.finalexam.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Mix implements Serializable {

    public String id;

    public String name;

    @Override
    public String toString() {
        return name;
    }

    public void addTrack(Track track){
        this.tracks.add(track);
    }

    public void removeTrack(Track track){
        this.tracks.remove(track);
    }

    public boolean self_owner;

    public String shared_by = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public ArrayList<Track> tracks = new ArrayList<>();

    public Mix(){}

}
