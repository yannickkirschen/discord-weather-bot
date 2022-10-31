package sh.yannick.bot.weather.calendar;

import java.time.LocalDate;

public interface LectureDayProvider {
    LectureDay retrieve(LocalDate date, String url) throws NoLecturesFoundException;
}
