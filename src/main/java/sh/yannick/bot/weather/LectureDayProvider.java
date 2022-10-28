package sh.yannick.bot.weather;

import sh.yannick.bot.weather.model.LectureDay;

import java.time.LocalDate;

public interface LectureDayProvider {
    LectureDay retrieve(LocalDate date, String url);
}
