package edu.uncc.finalexam.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Services {

    public static final String DB_USERS = "users";
    public static final String DB_MIXES = "mixes";

    public static class Albums{

        ArrayList<Album> albums = new ArrayList<>();

        public Albums(JSONObject output){
            try {
                JSONArray list = output.getJSONArray("data");
                for (int i = 0; i < list.length(); i++) {
                    this.albums.add(new Album(list.getJSONObject(i)));
                }
            } catch (JSONException exc) {
                exc.printStackTrace();
            }
        }

        public ArrayList<Album> getAlbums() {
            return albums;
        }

        public void setAlbums(ArrayList<Album> albums) {
            this.albums = albums;
        }

        @Override
        public String toString() {
            return "Albums{" +
                    "albums=" + albums +
                    '}';
        }
    }

    public static class Tracks{

        ArrayList<Track> tracks = new ArrayList<>();

        public Tracks(JSONObject output, String album_cover){
            try {
                JSONArray list = output.getJSONArray("data");
                for (int i = 0; i < list.length(); i++) {
                    this.tracks.add(new Track(list.getJSONObject(i), album_cover));
                }
            } catch (JSONException exc) {
                exc.printStackTrace();
            }
        }

        public ArrayList<Track> getTracks() {
            return tracks;
        }

        public void setTracks(ArrayList<Track> tracks) {
            this.tracks = tracks;
        }

        @Override
        public String toString() {
            return "Tracks{" +
                    "tracks=" + tracks +
                    '}';
        }
    }

}
