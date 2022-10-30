package sh.yannick.bot.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sh.yannick.bot.weather.calendar.LectureDay;
import sh.yannick.bot.weather.forecast.Forecast;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class MessageGenerator {
    public String generate(LectureDay lectureDay, List<Forecast> forecasts, Locale locale) {
        forecasts.sort(Comparator.comparing(Forecast::getDateTime));

        boolean precipitationBefore = false;
        boolean precipitationAfter = false;

        for (Forecast forecast : forecasts) {
            LocalTime forecastTime = forecast.getDateTime().toLocalTime();
            LocalTime beginTime = lectureDay.getBegin().toLocalTime();
            LocalTime endTime = lectureDay.getEnd().toLocalTime();

            if (!precipitationBefore
                && (forecastTime.isBefore(beginTime) || forecastTime.equals(beginTime))
                && ChronoUnit.HOURS.between(forecastTime, beginTime) <= 2 && forecast.isPrecipitation()) {
                precipitationBefore = true;
            }
            if (!precipitationAfter
                && (forecastTime.isAfter(endTime) || forecastTime.equals(endTime))
                && ChronoUnit.HOURS.between(forecastTime, endTime) <= 2 && forecast.isPrecipitation()) {
                precipitationAfter = true;
            }
        }

        String message;
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
        if (precipitationBefore && precipitationAfter) {
            message = messages.getString("precipitation.outward.return");
        } else if (precipitationBefore) {
            message = messages.getString("precipitation.outward");
        } else if (precipitationAfter) {
            message = messages.getString("precipitation.return");
        } else {
            message = messages.getString("precipitation.none");
        }

        log.info("Message is: {}", message);
        return message;
    }
}
