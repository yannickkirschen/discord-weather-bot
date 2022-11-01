package sh.yannick.bot.weather.forecast;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Value object for a single weather forecast.
 * <p>
 * Stores a timestamp as well as the expected precipitation.
 *
 * @author Yannick Kirschen
 * @since 1.0.0
 */
@Data
public class Forecast {
    private LocalDateTime dateTime;
    private boolean precipitation;
}
