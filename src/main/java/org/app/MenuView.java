package org.app;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuView {

    public VBox createMenu(
            Runnable openPlaylistAction,
            Runnable openTimerAction,
            Runnable exitAction
    ) {

        Label titleLabel = new Label("Study Hub");

        Button openPlaylistButton =
                new Button("Open Playlist");

        Button openTimerButton =
                new Button("Pomodoro Timer");

        Button exitButton =
                new Button("Exit");


        openPlaylistButton.setOnAction(
                e -> openPlaylistAction.run()
        );

        openTimerButton.setOnAction(
                e -> openTimerAction.run()
        );

        exitButton.setOnAction(
                e -> exitAction.run()
        );


        VBox menuLayout = new VBox();

        menuLayout.setSpacing(20);

        menuLayout.setAlignment(Pos.CENTER);


        menuLayout.getChildren().addAll(
                titleLabel,
                openPlaylistButton,
                openTimerButton,
                exitButton
        );


        return menuLayout;
    }
}