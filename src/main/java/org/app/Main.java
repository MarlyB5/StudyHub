package org.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.modules.ModuleView;
import org.playlist.PlaylistView;
import org.timer.TimerViewer;
import org.persistence.AppData;
import org.persistence.DataManager;

public class Main extends Application {

    private DataManager dataManager;
    private AppData appData;


    @Override
    public void start(Stage stage) {

        dataManager =
                new DataManager();

        appData =
                dataManager.loadData();


        Scene scene = new Scene(
                new Pane(),
                600,
                500
        );


        MenuView menuView =
                new MenuView();

        PlaylistView playlistView =
                new PlaylistView();

        TimerViewer timerView =
                new TimerViewer();

        ModuleView moduleView =
                new ModuleView();


        Pane[] menuRoot =
                new Pane[1];

        Pane[] playlistRoot =
                new Pane[1];

        Pane[] timerRoot =
                new Pane[1];

        Pane[] moduleRoot =
                new Pane[1];


        menuRoot[0] =
                menuView.createMenu(

                        () -> scene.setRoot(
                                playlistRoot[0]
                        ),

                        () -> scene.setRoot(
                                timerRoot[0]
                        ),

                        () -> scene.setRoot(
                                moduleRoot[0]
                        ),

                        () -> stage.close()
                );


        playlistRoot[0] =
                playlistView.createPlaylist(
                        appData,
                        () -> scene.setRoot(
                                menuRoot[0]
                        )
                );


        timerRoot[0] =
                timerView.createTimer(
                        appData,
                        () -> scene.setRoot(
                                menuRoot[0]
                        )
                );


        moduleRoot[0] =
                moduleView.createModuleView(
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

        stage.show();
    }


    @Override
    public void stop() {

        if (
                dataManager != null
                        && appData != null
        ) {

            dataManager.saveData(
                    appData
            );

            System.out.println(
                    "Application data saved on shutdown"
            );
        }
    }


    public static void main(String[] args) {

        launch();
    }
}