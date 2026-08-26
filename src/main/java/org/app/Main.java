package org.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.modules.ModuleView;
import org.playlist.PlaylistView;
import org.timer.TimerViewer;
import org.persistence.AppData;
import org.persistence.DataManager;
import org.dashboard.DashboardView;
import org.todo.TodoView;

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

        scene.getStylesheets().add(
                getClass()
                        .getResource(
                                "/styles/app.css"
                        )
                        .toExternalForm()
        );


        MenuView menuView =
                new MenuView();

        PlaylistView playlistView =
                new PlaylistView();

        TimerViewer timerView =
                new TimerViewer();

        ModuleView moduleView =
                new ModuleView();

        DashboardView dashboardView =
                new DashboardView();

        TodoView todoView =
                new TodoView();

        org.statistics.StatisticsView statisticsView =
                new org.statistics.StatisticsView();


        Parent[] menuRoot =
                new Parent[1];

        Parent[] playlistRoot =
                new Parent[1];

        Parent[] timerRoot =
                new Parent[1];

        Parent[] moduleRoot =
                new Parent[1];

        Parent[] dashboardRoot =
                new Parent[1];

        Parent[] tasksRoot =
                new Parent[1];

        Parent[] statisticsRoot =
                new Parent[1];

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

                        () -> {
                            // Refresh dashboard before showing
                            dashboardView.refreshProgress();
                            scene.setRoot(
                                    dashboardRoot[0]
                            );
                        },

                        () -> {
                            // Refresh tasks before showing (updates module list options)
                            todoView.refresh(appData);
                            scene.setRoot(
                                    tasksRoot[0]
                            );
                        },

                        () -> {
                            // Refresh statistics before showing
                            statisticsView.refreshStatistics();
                            scene.setRoot(
                                    statisticsRoot[0]
                            );
                        },

                        () -> stage.close()
                );


        {
            var content = playlistView.createPlaylist(
                    appData,
                    () -> scene.setRoot(
                            menuRoot[0]
                    )
            );
            ScrollPane sp = new ScrollPane(content);
            sp.setFitToWidth(true);
            sp.setFitToHeight(true);
            playlistRoot[0] = sp;
        }


        {
            var content = timerView.createTimer(
                    appData,
                    () -> scene.setRoot(
                            menuRoot[0]
                    )
            );
            ScrollPane sp = new ScrollPane(content);
            sp.setFitToWidth(true);
            timerRoot[0] = sp;
        }


        {
            var content = moduleView.createModuleView(
                    appData,
                    () -> scene.setRoot(
                            menuRoot[0]
                    )
            );
            ScrollPane sp = new ScrollPane(content);
            sp.setFitToWidth(true);
            moduleRoot[0] = sp;
        }

        {
            var content = dashboardView.createDashboard(
                    appData,
                    () -> scene.setRoot(
                            menuRoot[0]
                    )
            );
            ScrollPane sp = new ScrollPane(content);
            sp.setFitToWidth(true);
            dashboardRoot[0] = sp;
        }

        {
            var content = todoView.createTodoView(
                    appData,
                    () -> scene.setRoot(
                            menuRoot[0]
                    )
            );
            ScrollPane sp = new ScrollPane(content);
            sp.setFitToWidth(true);
            tasksRoot[0] = sp;
        }

        {
            var content = statisticsView.createStatistics(
                    appData,
                    () -> scene.setRoot(
                            menuRoot[0]
                    )
            );
            ScrollPane sp = new ScrollPane(content);
            sp.setFitToWidth(true);
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            statisticsRoot[0] = sp;
        }

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