package sh.yannick.bot.weather;

import sh.yannick.bot.weather.model.Forecast;

import java.time.LocalDate;
import java.util.List;

public interface WeatherForecastProvider {
    List<Forecast> retrieve(LocalDate date, String url);
}
