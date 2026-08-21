package org;

import javafx.beans.Observable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MediaManager {

    private double currentVolume = 0.5;

    private MediaPlayer mediaPlayer;


    public void playSong(Song song, Runnable whenFinished, ProgressListener progressListener) {

        if (song == null) {
            System.out.println("Cannot play song: song is null");
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        String fileLocation = song.getFileLocation();

        if (fileLocation == null || fileLocation.isBlank()) {
            System.out.println("Cannot play song: file location is empty");
            return;
        }

        try {
            String mediaSource;
            /* Uploaded songs created by FileChooser already use a file URI,
              such as file:/C:/Users/Name/Music/song.mp3.
             */
            if (fileLocation.startsWith("file:")) {
                mediaSource = fileLocation;
            } else {
                URL resource = getClass().getResource(fileLocation);

                if (resource == null) {
                    System.out.println(
                            "Could not find resource: " + fileLocation
                    );
                    return;
                }

                mediaSource = resource.toExternalForm();
            }

            System.out.println("Attempting to play:");
            System.out.println(mediaSource);

            Media media = new Media(mediaSource);

            media.setOnError(() -> {
                System.out.println("Media error:");
                System.out.println(media.getError());
            });

            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.currentTimeProperty().addListener(
                    (Observable,oldTime,newTime) -> {

                        if ( progressListener != null && mediaPlayer.getTotalDuration() != null
                            && !mediaPlayer.getTotalDuration().isUnknown())
                        {
                            progressListener.Progress(
                                    newTime.toSeconds(),
                                    mediaPlayer.getTotalDuration().toSeconds());
                        }
                    }
            );


            mediaPlayer.setVolume(currentVolume);

            mediaPlayer.setOnReady(() -> {
                System.out.println("Media is ready");
                System.out.println(
                        "Duration: " + media.getDuration()
                );

                mediaPlayer.play();
            });

            mediaPlayer.setOnPlaying(() ->
                    System.out.println("Song is now playing")
            );

            mediaPlayer.setOnPaused(() ->
                    System.out.println("Song has been paused")
            );

            mediaPlayer.setOnStopped(() ->
                    System.out.println("Song has been stopped")
            );

            mediaPlayer.setOnError(() -> {
                System.out.println("MediaPlayer error:");
                System.out.println(mediaPlayer.getError());
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("Song finished");

                if (whenFinished != null) {
                    whenFinished.run();
                }
            });

        } catch (MediaException exception) {
            System.out.println("JavaFX could not load the media file:");
            exception.printStackTrace();
        } catch (Exception exception) {
            System.out.println("Unexpected error while playing song:");
            exception.printStackTrace();
        }
    }

    public void pauseSong() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        } else {
            System.out.println("Cannot pause: no MediaPlayer exists");
        }
    }

    public void resumeSong() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        } else {
            System.out.println("Cannot resume: no MediaPlayer exists");
        }
    }

    public void setVolume(double volume) {
        /*
         * Keeps the volume safely between 0.0 and 1.0.
         */
        currentVolume = Math.max(0.0, Math.min(1.0, volume));

        if (mediaPlayer != null) {
            mediaPlayer.setVolume(currentVolume);
        }
    }

    public void seek(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(
                    javafx.util.Duration.seconds(seconds)
            );
        }
    }

    public double getTotalDurationSeconds() {

        if (
                mediaPlayer == null
                        || mediaPlayer.getTotalDuration() == null
                        || mediaPlayer.getTotalDuration().isUnknown()
        ) {
            return 0;
        }

        return mediaPlayer
                .getTotalDuration()
                .toSeconds();
    }
}

