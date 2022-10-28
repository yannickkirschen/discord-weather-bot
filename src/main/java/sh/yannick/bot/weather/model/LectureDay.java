package sh.yannick.bot.weather.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LectureDay {
    private LocalDateTime begin;
    private LocalDateTime end;
}
