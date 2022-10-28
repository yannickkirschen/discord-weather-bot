package sh.yannick.bot.weather.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Forecast {
    private LocalDateTime dateTime;
    private boolean precipitation;
}
