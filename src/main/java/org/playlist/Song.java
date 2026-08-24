package org.playlist;
//songs class for storing songs
public class Song {

    private String title;
    private String artist;
    private String fileLocation;

    public Song() {

    }

    public Song(String title, String artist, String fileLocation) {
        this.title = title;
        this.artist = artist;
        this.fileLocation = fileLocation;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}
