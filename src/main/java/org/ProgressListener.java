package org;

public interface ProgressListener {


    void Progress(
            double currentProgress,
            double totalDuration
    );
}
