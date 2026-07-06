package org;

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

    public BorderPane createPlaylist(Runnable backAction) {
        Label titleLabel = new Label("My Playlist");
        Label emptyLabel = new Label("No songs in playlist");
        Label nowPlayingLabel = new Label("Now Playing: ");

        ListView<Song>playlist = new ListView<>();
        playlist.setItems(playlistManager.getSongs());

        Button backButton = new Button("Back");
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button nextButton = new Button("Next");
        Button prevButton = new Button("Previous");
        Button shuffleButton = new Button("Shuffle");
        Button removeButton = new Button("Remove");
        Button addSongButton = new Button("Add Song");

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
                mediaManager.playSong(currentSong);
                nowPlayingLabel.setText("Now Playing: " + currentSong.getTitle());
            } else {
                nowPlayingLabel.setText("No songs in playlist");
            }

        });

        pauseButton.setOnAction(e -> {
            mediaManager.pauseSong();
            nowPlayingLabel.setText("Paused");
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

        HBox controls = new HBox(10, prevButton, playButton, pauseButton, nextButton, backButton, addSongButton, removeButton, shuffleButton);

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
}