package org.playlist;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class PlaylistManager {

    private final ObservableList<Song> songs;

    public PlaylistManager() {
        songs = FXCollections.observableArrayList();
        loadDefaultSongs();

    }

    private void loadDefaultSongs(){
        songs.add(new Song("Sample Song 1", "Unknown Artist", "/SampleSong1.mp3"));
        songs.add(new Song("Sample Song 2", "Unknown Artist", "/SampleSong2.mp3"));

    }

    public ObservableList<Song> getSongs(){
        return songs;
    }

    public void removeSong(Song song){
        songs.remove(song);
    }

    public void addSong(Song song){
        songs.add(song);
    }
}
