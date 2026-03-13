package org;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuView {

    public VBox createMenu(Runnable openPlaylistAction, Runnable exitAction) {
        Label titleLabel = new Label("Study Playlist App");

        Button openPlaylistButton = new Button("Open Playlist");
        Button exitButton = new Button("Exit");

        openPlaylistButton.setOnAction(e -> openPlaylistAction.run());
        exitButton.setOnAction(e -> exitAction.run());

        VBox menuLayout = new VBox();
        menuLayout.setSpacing(20);
        menuLayout.setAlignment(Pos.CENTER);

        menuLayout.getChildren().addAll(
                titleLabel,
                openPlaylistButton,
                exitButton
        );

        return menuLayout;
    }
}