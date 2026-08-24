package org.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.playlist.PlaylistView;
import org.timer.TimerViewer;
import org.persistence.AppData;
import org.persistence.DataManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        DataManager dataManager =
                new DataManager();

        AppData appData =
                dataManager.loadData();

        Scene scene = new Scene(
                new Pane(),
                600,
                500
        );

        MenuView menuView = new MenuView();
        PlaylistView playlistView = new PlaylistView();
        TimerViewer timerView = new TimerViewer();

        Pane[] menuRoot = new Pane[1];
        Pane[] playlistRoot = new Pane[1];
        Pane[] timerRoot = new Pane[1];

        menuRoot[0] = menuView.createMenu(

                () -> scene.setRoot(
                        playlistRoot[0]
                ),

                () -> scene.setRoot(
                        timerRoot[0]
                ),

                () -> stage.close()
        );

        playlistRoot[0] = playlistView.createPlaylist(
                appData,
                () -> scene.setRoot(
                        menuRoot[0]
                )
        );

        timerRoot[0] = timerView.createTimer(
                appData,
                () -> scene.setRoot(
                        menuRoot[0]
                )
        );

        scene.setRoot(
                menuRoot[0]
        );

        stage.setTitle(
                "Study Hub"
        );

        stage.setScene(
                scene
        );

        stage.setOnCloseRequest(event -> {

            dataManager.saveData(
                    appData
            );
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}