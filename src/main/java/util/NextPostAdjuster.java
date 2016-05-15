package util;

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
    public static final Duration DELAY = Duration.ofHours(4);
    @Override
    public Temporal adjustInto(Temporal input) {
        LocalDateTime timestamp = LocalDateTime.from(input);
        timestamp = timestamp.plus(DELAY);
        return input.with(timestamp);
    }
}
