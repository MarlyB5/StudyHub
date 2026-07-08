
package org;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Pane(), 600, 500);

        MenuView menuView = new MenuView();
        PlaylistView playlistView = new PlaylistView();

        Pane[] menuRoot = new Pane[1];
        Pane[] playlistRoot = new Pane[1];

        menuRoot[0] = menuView.createMenu(
                () -> scene.setRoot(playlistRoot[0]),
                () -> stage.close()
        );

        playlistRoot[0] = playlistView.createPlaylist(
                () -> scene.setRoot(menuRoot[0])
        );

        scene.setRoot(menuRoot[0]);

        stage.setTitle("Study Playlist App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}