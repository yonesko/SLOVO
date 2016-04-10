package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

/**
 * Created by gleb on 10.04.16.
 */

/**
 * adds 5 hours
 */
public class NextPostAdjuster implements TemporalAdjuster {
    private static final int DELAY_H = 5;
    @Override
    public Temporal adjustInto(Temporal input) {
        LocalDateTime timestamp = LocalDateTime.from(input);
        timestamp = timestamp.plusHours(DELAY_H);
        return input.with(timestamp);
    }
}
