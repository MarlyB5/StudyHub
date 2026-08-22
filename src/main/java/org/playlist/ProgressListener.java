package org.playlist;

public interface ProgressListener {


    void Progress(
            double currentProgress,
            double totalDuration
    );
}
