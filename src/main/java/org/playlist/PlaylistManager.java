package org.playlist;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import org.persistence.AppData;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {

    private final ObservableList<Song> songs;
    private final AppData appData;

    // Existing constructor preserved for tests and standalone use
    public PlaylistManager() {
        this.appData = null;
        songs = FXCollections.observableArrayList();
        loadDefaultSongs();
    }

    // New constructor that wires to shared AppData for persistence
    public PlaylistManager(AppData appData) {
        this.appData = appData;
        songs = FXCollections.observableArrayList();

        if (appData == null) {
            // Fallback to defaults if no AppData provided
            loadDefaultSongs();
            return;
        }

        // Ensure list exists in AppData
        List<Song> saved = appData.getPlaylistSongs();
        if (saved == null) {
            saved = new ArrayList<>();
            appData.setPlaylistSongs(saved);
        }

        if (appData.isPlaylistInitialized()) {
            // Use whatever is saved, even if empty (user cleared playlist)
            songs.addAll(saved);
        } else {
            // First run: seed defaults into both in-memory and persisted list
            loadDefaultSongs();
            // Mirror into AppData
            saved.clear();
            saved.addAll(songs);
            appData.setPlaylistInitialized(true);
        }
    }

    private void loadDefaultSongs(){
        songs.add(new Song("Sample Song 1", "Unknown Artist", "/SampleSong1.mp3"));
        songs.add(new Song("Sample Song 2", "Unknown Artist", "/SampleSong2.mp3"));
    }

    public ObservableList<Song> getSongs(){
        return songs;
    }

    public void removeSong(Song song){
        if (song == null) return;
        songs.remove(song);
        if (appData != null && appData.getPlaylistSongs() != null) {
            appData.getPlaylistSongs().removeIf(s ->
                    safeEq(s.getTitle(), song.getTitle()) &&
                    safeEq(s.getArtist(), song.getArtist()) &&
                    safeEq(s.getFileLocation(), song.getFileLocation())
            );
        }
    }

    public void addSong(Song song){
        if (song == null) return;
        songs.add(song);
        if (appData != null) {
            List<Song> list = appData.getPlaylistSongs();
            if (list == null) {
                list = new ArrayList<>();
                appData.setPlaylistSongs(list);
            }
            list.add(song);
            appData.setPlaylistInitialized(true);
        }
    }

    private boolean safeEq(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }
}
