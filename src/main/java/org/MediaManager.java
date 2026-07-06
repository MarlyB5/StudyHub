package org;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class MediaManager {

    private MediaPlayer mediaPlayer;

    public void playSong(Song song){

        if (song == null){
            return;
        }

        if (mediaPlayer != null){
            mediaPlayer.stop();
        }

        String fileLocation = song.getFileLocation();

        Media media;

        if (fileLocation.startsWith("file:")){
            media = new Media(fileLocation);
        }else {
            String path = getClass().getResource(fileLocation).toExternalForm();
            media = new Media(path);
        }

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void pauseSong(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
        }
    }
}
