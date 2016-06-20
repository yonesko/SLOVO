package util;

import main.PropManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

/**
 * Created by gleb on 10.04.16.
 */

/**
 * adds DELAY
 */
public class NextPostAdjuster implements TemporalAdjuster {
    private Duration delay =
            Duration.ofHours(Long.parseLong(PropManager.getProp("postAlgo.nextPostDelay")));
    @Override
    public Temporal adjustInto(Temporal input) {
        LocalDateTime timestamp = LocalDateTime.from(input);
        timestamp = timestamp.plus(delay);
        return input.with(timestamp);
    }

    public Duration getDelay() {
        return delay;
    }
}
