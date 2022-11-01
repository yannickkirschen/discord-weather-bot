package sh.yannick.bot.weather.calendar;

import java.time.LocalDate;

/**
 * Provides a {@link LectureDay}.
 * <p>
 * Implementations of this interface provide begin and end of a lecture day. To do so, they may query any kind of
 * datasource. The beginning of a lecture day is the beginning of the first lecture. The end of a lecture day is the end
 * of the last lecture.
 *
 * @author Yannick Kirschen
 * @since 1.0.0
 */
@FunctionalInterface
public interface LectureDayProvider {
    /**
     * Retrieves beginning and end times for s single lecture day.
     *
     * @param date date to get the beginning and end for.
     * @return beginning and end of a single lecture day for the given date
     * @throws NoLecturesFoundException if there are no lectures in the calendar for the given date
     */
    LectureDay retrieve(LocalDate date) throws NoLecturesFoundException;
}
