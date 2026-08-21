package org;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class PlaylistView {

    private final MediaManager mediaManager = new MediaManager();
    private final PlaylistManager playlistManager = new PlaylistManager();

    private boolean isPaused = false;
    private int currentSongIndex = 0;

    public BorderPane createPlaylist(Runnable backAction) {

        // ------------------------------------------------------------
        // LABELS
        // ------------------------------------------------------------

        Label titleLabel = new Label("My Playlist");

        Label nowPlayingLabel = new Label("Now Playing: ");

        Label timeLabel = new Label("0:00 / 0:00");

        Label volumeLabel = new Label("Volume");


        // ------------------------------------------------------------
        // PLAYLIST
        // ------------------------------------------------------------

        ListView<Song> playlist = new ListView<>();

        playlist.setItems(
                playlistManager.getSongs()
        );

        // PROGRESS SLIDER
        Slider progressSlider = new Slider(
                0,
                100,
                0
        );


        progressSlider.setOnMouseReleased(event -> {

            Song currentSong = getCurrentSong();

            if (currentSong == null) {
                return;
            }

            double totalSeconds = mediaManager.getTotalDurationSeconds();

            double selectedPercentage =
                    progressSlider.getValue() / 100.0;

            double selectedSeconds =
                    selectedPercentage * totalSeconds;

            mediaManager.seek(selectedSeconds);
        });


        // VOLUME SLIDER

        Slider volumeSlider = new Slider(
                0.0,
                1.0,
                0.5
        );

        volumeSlider.setPrefWidth(200);

        volumeSlider
                .valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            mediaManager.setVolume(
                                    newValue.doubleValue()
                            );
                        }
                );


     // BUTTONS

        Button backButton = new Button("Back");

        Button playButton = new Button("Play");

        Button pauseButton = new Button("Pause");

        Button nextButton = new Button("Next");

        Button previousButton = new Button("Previous");

        Button shuffleButton = new Button("Shuffle");

        Button removeButton = new Button("Remove");

        Button addSongButton = new Button("Add Song");


        // DOUBLE CLICK TO PLAY

        playlist.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {

                Song selectedSong =
                        playlist
                                .getSelectionModel()
                                .getSelectedItem();

                if (selectedSong != null) {

                    currentSongIndex =
                            playlist
                                    .getSelectionModel()
                                    .getSelectedIndex();

                    playSong(
                            selectedSong,
                            playlist,
                            nowPlayingLabel,
                            progressSlider,
                            timeLabel
                    );
                }
            }
        });


        // BACK BUTTON

        backButton.setOnAction(event -> {

            if (backAction != null) {
                backAction.run();
            }
        });


        // ADD SONG

        addSongButton.setOnAction(event -> {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Choose an audio file"
            );

            fileChooser
                    .getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "Audio Files",
                                    "*.mp3",
                                    "*.wav"
                            )
                    );

            File selectedFile =
                    fileChooser.showOpenDialog(null);

            if (selectedFile != null) {

                Song newSong = new Song(
                        selectedFile.getName(),
                        "Unknown Artist",
                        selectedFile
                                .toURI()
                                .toString()
                );

                playlistManager.addSong(
                        newSong
                );
            }
        });

        // PLAY BUTTON

        playButton.setOnAction(event -> {

            Song selectedSong =
                    playlist
                            .getSelectionModel()
                            .getSelectedItem();

            if (selectedSong != null) {

                currentSongIndex =
                        playlist
                                .getSelectionModel()
                                .getSelectedIndex();

                playSong(
                        selectedSong,
                        playlist,
                        nowPlayingLabel,
                        progressSlider,
                        timeLabel
                );

            } else {

                nowPlayingLabel.setText(
                        "Please select a song"
                );
            }
        });


        // PAUSE / RESUME BUTTON

        pauseButton.setOnAction(event -> {

            if (isPaused) {

                mediaManager.resumeSong();

                pauseButton.setText(
                        "Pause"
                );

                Song currentSong =
                        getCurrentSong();

                if (currentSong != null) {

                    nowPlayingLabel.setText(
                            "Now Playing: "
                                    + currentSong.getTitle()
                    );
                }

                isPaused = false;

            } else {

                mediaManager.pauseSong();

                pauseButton.setText(
                        "Resume"
                );

                Song currentSong =
                        getCurrentSong();

                if (currentSong != null) {

                    nowPlayingLabel.setText(
                            "Paused: "
                                    + currentSong.getTitle()
                    );
                }

                isPaused = true;
            }
        });


        // REMOVE SONG

        removeButton.setOnAction(event -> {

            Song selectedSong =
                    playlist
                            .getSelectionModel()
                            .getSelectedItem();

            if (selectedSong != null) {

                playlistManager.removeSong(
                        selectedSong
                );

                nowPlayingLabel.setText(
                        "Removed: "
                                + selectedSong.getTitle()
                );


                if (
                        currentSongIndex
                                >= playlistManager
                                .getSongs()
                                .size()
                ) {
                    currentSongIndex =
                            playlistManager
                                    .getSongs()
                                    .size() - 1;
                }

            } else {

                nowPlayingLabel.setText(
                        "Please select a song to remove"
                );
            }
        });


        // NEXT SONG

        nextButton.setOnAction(event -> {

            playNextSong(
                    playlist,
                    nowPlayingLabel,
                    progressSlider,
                    timeLabel
            );
        });


        // PREVIOUS SONG


        previousButton.setOnAction(event -> {

            playPreviousSong(
                    playlist,
                    nowPlayingLabel,
                    progressSlider,
                    timeLabel
            );
        });


        // ------------------------------------------------------------
        // SHUFFLE BUTTON
        // ------------------------------------------------------------

        /*
         * The button is restored,
         * but we are NOT adding shuffle logic yet.
         *
         * We will implement that after progress/seeking works.
         */
        shuffleButton.setOnAction(event -> {

            nowPlayingLabel.setText(
                    "Shuffle not implemented yet"
            );
        });


        // ------------------------------------------------------------
        // LAYOUT
        // ------------------------------------------------------------

        HBox controls = new HBox(
                10,
                previousButton,
                playButton,
                pauseButton,
                nextButton,
                shuffleButton,
                addSongButton,
                removeButton,
                backButton
        );


        VBox progressSection = new VBox(
                5,
                timeLabel,
                progressSlider
        );


        VBox volumeSection = new VBox(
                5,
                volumeLabel,
                volumeSlider
        );


        VBox topSection = new VBox(
                10,
                titleLabel,
                nowPlayingLabel
        );

        topSection.setPadding(
                new Insets(15)
        );


        VBox bottomSection = new VBox(
                10,
                progressSection,
                volumeSection,
                controls
        );

        bottomSection.setPadding(
                new Insets(10)
        );


        BorderPane playlistLayout =
                new BorderPane();

        playlistLayout.setTop(
                topSection
        );

        playlistLayout.setCenter(
                playlist
        );

        playlistLayout.setBottom(
                bottomSection
        );


        BorderPane.setMargin(
                playlist,
                new Insets(10)
        );


        return playlistLayout;
    }


    // ============================================================
    // PLAY SONG
    // ============================================================

    /*
     * This helper method stops us from repeating the same
     * MediaManager.playSong() code everywhere.
     */
    private void playSong(
            Song song,
            ListView<Song> playlist,
            Label nowPlayingLabel,
            Slider progressSlider,
            Label timeLabel
    ) {

        mediaManager.playSong(

                song,

                // Runs when the song finishes
                () -> playNextSong(
                        playlist,
                        nowPlayingLabel,
                        progressSlider,
                        timeLabel
                ),

                // Runs whenever the playback time changes
                (currentSeconds, totalSeconds) -> {

                    updateProgress(
                            progressSlider,
                            timeLabel,
                            currentSeconds,
                            totalSeconds
                    );
                }
        );

        isPaused = false;

        nowPlayingLabel.setText(
                "Now Playing: "
                        + song.getTitle()
        );

        /*
         * Reset progress immediately when a new song starts.
         */
        progressSlider.setValue(0);

        timeLabel.setText(
                "0:00 / 0:00"
        );
    }


    // ============================================================
    // NEXT SONG
    // ============================================================

    private void playNextSong(
            ListView<Song> playlist,
            Label nowPlayingLabel,
            Slider progressSlider,
            Label timeLabel
    ) {

        if (
                playlistManager
                        .getSongs()
                        .isEmpty()
        ) {

            nowPlayingLabel.setText(
                    "No songs in playlist"
            );

            return;
        }


        currentSongIndex++;


        /*
         * If we go past the final song,
         * return to the first song.
         */
        if (
                currentSongIndex
                        >= playlistManager
                        .getSongs()
                        .size()
        ) {

            currentSongIndex = 0;
        }


        Song nextSong =
                playlistManager
                        .getSongs()
                        .get(currentSongIndex);


        playlist
                .getSelectionModel()
                .select(currentSongIndex);


        playSong(
                nextSong,
                playlist,
                nowPlayingLabel,
                progressSlider,
                timeLabel
        );
    }


    // ============================================================
    // PREVIOUS SONG
    // ============================================================

    private void playPreviousSong(
            ListView<Song> playlist,
            Label nowPlayingLabel,
            Slider progressSlider,
            Label timeLabel
    ) {

        if (
                playlistManager
                        .getSongs()
                        .isEmpty()
        ) {

            nowPlayingLabel.setText(
                    "No songs in playlist"
            );

            return;
        }


        currentSongIndex--;


        /*
         * If we go before the first song,
         * wrap around to the final song.
         */
        if (currentSongIndex < 0) {

            currentSongIndex =
                    playlistManager
                            .getSongs()
                            .size() - 1;
        }


        Song previousSong =
                playlistManager
                        .getSongs()
                        .get(currentSongIndex);


        playlist
                .getSelectionModel()
                .select(currentSongIndex);


        playSong(
                previousSong,
                playlist,
                nowPlayingLabel,
                progressSlider,
                timeLabel
        );
    }


    // ============================================================
    // PROGRESS
    // ============================================================

    private void updateProgress(
            Slider progressSlider,
            Label timeLabel,
            double currentSeconds,
            double totalSeconds
    ) {

        if (totalSeconds <= 0) {
            return;
        }


        double progressPercentage =
                currentSeconds
                        / totalSeconds
                        * 100;


        progressSlider.setValue(
                progressPercentage
        );


        String currentTime =
                formatTime(
                        currentSeconds
                );


        String totalTime =
                formatTime(
                        totalSeconds
                );


        timeLabel.setText(
                currentTime
                        + " / "
                        + totalTime
        );
    }

    // FORMAT TIME


    private String formatTime(
            double seconds
    ) {

        int totalSeconds =
                (int) seconds;


        int minutes =
                totalSeconds / 60;


        int remainingSeconds =
                totalSeconds % 60;


        return String.format(
                "%d:%02d",
                minutes,
                remainingSeconds
        );
    }
    // GET CURRENT SONG

    private Song getCurrentSong() {

        if (
                playlistManager
                        .getSongs()
                        .isEmpty()
        ) {
            return null;
        }


        if (
                currentSongIndex < 0
                        || currentSongIndex
                        >= playlistManager
                        .getSongs()
                        .size()
        ) {
            return null;
        }


        return playlistManager
                .getSongs()
                .get(currentSongIndex);
    }
}