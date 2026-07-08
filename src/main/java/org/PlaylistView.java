package org;

import javafx.scene.control.Slider;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;

public class PlaylistView {

    private final MediaManager mediaManager = new MediaManager();
    private final PlaylistManager playlistManager = new PlaylistManager();

    private boolean isPaused = false;
    private int currentSongIndex = 0; // Track the current song index

    public BorderPane createPlaylist(Runnable backAction) {
        Label titleLabel = new Label("My Playlist");
        Label emptyLabel = new Label("No songs in playlist");
        Label nowPlayingLabel = new Label("Now Playing: ");

        ListView<Song>playlist = new ListView<>();
        playlist.setItems(playlistManager.getSongs());

        playlist.setOnMouseClicked(e -> {

            if (e.getClickCount() == 2) {
                Song selectedSong = playlist.getSelectionModel().getSelectedItem();


                if (selectedSong != null) {
                  currentSongIndex = playlist.getSelectionModel().getSelectedIndex();

                  mediaManager.playSong(selectedSong, null);
                  nowPlayingLabel.setText("Now Playing: " + selectedSong.getTitle());
                }
            }
        });

        Button backButton = new Button("Back");
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button nextButton = new Button("Next");
        Button prevButton = new Button("Previous");
        Button shuffleButton = new Button("Shuffle");
        Button removeButton = new Button("Remove");
        Button addSongButton = new Button("Add Song");


        // BUTTON CODE ------------------------------------------------
        backButton.setOnAction(e -> backAction.run());

        addSongButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose an MP3 file");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")
            );

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                Song newSong = new Song(
                        selectedFile.getName(),
                        "Unknown Artist",
                        selectedFile.toURI().toString()
                );
                playlistManager.addSong(newSong);
            }
        });


        playButton.setOnAction(e -> {
            Song currentSong = playlist.getSelectionModel().getSelectedItem();

            if (currentSong != null){
                currentSongIndex = playlist.getSelectionModel().getSelectedIndex();

                mediaManager.playSong(currentSong, () -> playNextSong(playlist, nowPlayingLabel));
                isPaused = false;
                playButton.setText("Pause");

                nowPlayingLabel.setText("Now Playing: " + currentSong.getTitle());
            } else {
                nowPlayingLabel.setText("No songs in playlist");
            }

        });

        pauseButton.setOnAction(e -> {
           if (isPaused){
               mediaManager.resumeSong();
               pauseButton.setText("Pause");
               nowPlayingLabel.setText("Now Playing: " + playlist.getSelectionModel().getSelectedItem().getTitle());
               isPaused = false;
           } else {
               mediaManager.pauseSong();
               pauseButton.setText("Resume");
               nowPlayingLabel.setText("Paused: " + playlist.getSelectionModel().getSelectedItem().getTitle());
               isPaused = true;

           }

        });

        removeButton.setOnAction(e -> {
            Song currentSong = playlist.getSelectionModel().getSelectedItem();

            if (currentSong != null){
                playlistManager.removeSong(currentSong);
                nowPlayingLabel.setText("Removed: " + currentSong.getTitle());
            } else{
                nowPlayingLabel.setText("Please select a song to remove");
            }
        });

        nextButton.setOnAction(e -> {
            playNextSong(playlist, nowPlayingLabel);
        });

        prevButton.setOnAction(e -> {
            playPreviousSong(playlist, nowPlayingLabel);
        });


        Label volumeLabel = new Label("Volume: ");

        Slider volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100;
            mediaManager.setVolume(volume);
        });



        HBox controls = new HBox(10, prevButton, playButton, pauseButton, nextButton, backButton, addSongButton, removeButton, shuffleButton);
        VBox bottomSection = new VBox(10, controls, volumeLabel, volumeSlider);
        VBox topSection = new VBox(10, titleLabel, nowPlayingLabel);
        topSection.setPadding(new Insets(15));

        BorderPane playlistLayout = new BorderPane();
        playlistLayout.setTop(topSection);
        playlistLayout.setCenter(playlist);
        playlistLayout.setBottom(controls);

        BorderPane.setMargin(playlist, new Insets(10));
        BorderPane.setMargin(controls, new Insets(10));

        return playlistLayout;
    }

    private void playNextSong(ListView<Song> playlist, Label nowPlayingLabel) {
        if(playlistManager.getSongs().isEmpty()) {
            nowPlayingLabel.setText("No songs in playlist");
            return;
        }

        currentSongIndex ++;

        if (currentSongIndex >= playlistManager.getSongs().size()) {
            currentSongIndex = 0;
        }
        Song nextSong = playlistManager.getSongs().get(currentSongIndex);
        mediaManager.playSong(nextSong, () -> playNextSong(playlist, nowPlayingLabel));
        isPaused = false;
        nowPlayingLabel.setText("Now Playing: " + nextSong.getTitle());
    }

    private void playPreviousSong(ListView<Song> playlist, Label nowPlayingLabel) {
        if(playlistManager.getSongs().isEmpty()) {
            nowPlayingLabel.setText("No songs in playlist");
            return;
        }

        currentSongIndex --;

        if (currentSongIndex < 0) {
            currentSongIndex = playlistManager.getSongs().size() - 1;
        }

        Song previousSong = playlistManager.getSongs().get(currentSongIndex);

        playlist.getSelectionModel().select(currentSongIndex);
        mediaManager.playSong(previousSong, () -> playPreviousSong(playlist, nowPlayingLabel));
        isPaused = false;
        nowPlayingLabel.setText("Now Playing: " + previousSong.getTitle());
    }
}