package org.playlist;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    @Test
    void gettersAndToStringWork() {
        Song s = new Song("Title A", "Artist B", "/music/a.mp3");
        assertEquals("Title A", s.getTitle());
        assertEquals("Artist B", s.getArtist());
        assertEquals("/music/a.mp3", s.getFileLocation());
        assertEquals("Title A - Artist B", s.toString());
    }
}
