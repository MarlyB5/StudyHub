package org.playlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistManagerTest {

    private PlaylistManager manager;

    @BeforeEach
    void setUp() {
        manager = new PlaylistManager();
    }

    @Test
    void defaultSongsLoaded() {
        assertNotNull(manager.getSongs());
        assertTrue(manager.getSongs().size() >= 2, "Expected at least two default sample songs");
        assertEquals("Sample Song 1 - Unknown Artist", manager.getSongs().get(0).toString());
        assertEquals("Sample Song 2 - Unknown Artist", manager.getSongs().get(1).toString());
    }

    @Test
    void addAndRemoveSong() {
        int initialSize = manager.getSongs().size();
        Song s = new Song("New", "Artist", "/new.mp3");
        manager.addSong(s);
        assertEquals(initialSize + 1, manager.getSongs().size());
        assertTrue(manager.getSongs().contains(s));

        manager.removeSong(s);
        assertEquals(initialSize, manager.getSongs().size());
        assertFalse(manager.getSongs().contains(s));
    }
}
