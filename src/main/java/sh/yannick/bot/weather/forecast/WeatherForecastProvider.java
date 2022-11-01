package sh.yannick.bot.weather.forecast;

import java.time.LocalDate;
import java.util.List;

/**
 * Provides a list of weather {@link Forecast}s.
 * <p>
 * Implementations query a weather service and aggregate its data into a list of weather {@link Forecast}s, providing
 * {@link java.time.LocalDateTime} and expected precipitation.
 *
 * @author Yannick Kirschen
 * @since 1.0.0
 */
@FunctionalInterface
public interface WeatherForecastProvider {
    /**
     * Retrieves a list of weather {@link Forecast}s for a given date.
     *
     * @param date date to get the forecasts for
     * @return a list of forecasts for the given date
     */
    List<Forecast> retrieve(LocalDate date);
}
