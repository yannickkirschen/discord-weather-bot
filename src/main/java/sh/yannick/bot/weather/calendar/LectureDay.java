package sh.yannick.bot.weather.calendar;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Value object for a single lecture day.
 * <p>
 * Stores the beginning and the end of a lecture day.
 *
 * @author Yannick Kirschen
 * @since 1.0.0
 */
@Data
public class LectureDay {
    private LocalDateTime begin;
    private LocalDateTime end;
}
