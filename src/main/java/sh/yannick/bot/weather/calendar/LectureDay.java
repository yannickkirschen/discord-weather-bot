package sh.yannick.bot.weather.calendar;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LectureDay {
    private LocalDateTime begin;
    private LocalDateTime end;
}
