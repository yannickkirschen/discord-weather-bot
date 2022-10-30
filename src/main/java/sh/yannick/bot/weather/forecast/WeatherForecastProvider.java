package sh.yannick.bot.weather.forecast;

import java.time.LocalDate;
import java.util.List;

public interface WeatherForecastProvider {
    List<Forecast> retrieve(LocalDate date, String url);
}
