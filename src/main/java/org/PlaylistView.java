package org;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlaylistView {

    public BorderPane createPlaylist(Runnable backAction) {
        Label titleLabel = new Label("My Playlist");

        ListView<Song>playlist = new ListView<>();
        playlist.getItems().addAll(

                //COME BACK TO THIS LATER

        );

        Button backButton = new Button("Back");
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button nextButton = new Button("Next");
        Button prevButton = new Button("Previous");

        backButton.setOnAction(e -> backAction.run());

        HBox controls = new HBox(10, prevButton, playButton, pauseButton, nextButton, backButton);

        VBox topSection = new VBox(10, titleLabel);
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