package org.timer;

// communcation interface for timer
public interface TimeListener {

    void onTimerUpdate(
            int secondsRemaining,
            boolean workSession
    );
}
