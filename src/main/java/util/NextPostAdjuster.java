package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

/**
 * Created by gleb on 10.04.16.
 */

/**
 * adds DELAY_H hours
 */
public class NextPostAdjuster implements TemporalAdjuster {
    private static final int DELAY_H = 4;
    @Override
    public Temporal adjustInto(Temporal input) {
        LocalDateTime timestamp = LocalDateTime.from(input);
        timestamp = timestamp.plusHours(DELAY_H);
        return input.with(timestamp);
    }
}
