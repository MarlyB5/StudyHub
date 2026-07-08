package org;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

public class MediaManager {

    private MediaPlayer mediaPlayer;

    public void playSong(Song song, Runnable whenFinished){

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
            String resourcePath = getClass().getResource(fileLocation).toExternalForm();
            media = new Media(resourcePath);
        }

        mediaPlayer = new MediaPlayer(media);


        mediaPlayer.setOnEndOfMedia(()-> {
            if (whenFinished != null){
                whenFinished.run();
            }
                });

        mediaPlayer.play();
    }

    public void pauseSong(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    public void resumeSong(){
        if (mediaPlayer != null){
            mediaPlayer.play();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null){
            mediaPlayer.setVolume(volume);
        }
    }
}
