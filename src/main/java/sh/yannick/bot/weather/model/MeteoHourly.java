package sh.yannick.bot.weather.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeteoHourly {
    private List<LocalDateTime> time;
    private List<Double> rain;
    private List<Double> showers;
    private List<Double> snowfall;
}
