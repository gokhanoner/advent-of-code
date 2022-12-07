package org.oner.utils;

import java.time.Duration;
import java.time.Instant;

public class Timer {

    public static void timeIt(Runnable job) {
        Instant start = Instant.now();
        try {
            job.run();
        } finally {
            System.out.println("Took %s".formatted(Duration.between(start, Instant.now())));
        }
    }
}
