package sh.yannick.bot.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sh.yannick.bot.weather.model.*;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class MessageGenerator {
    public String generate(LectureDay lectureDay, List<Forecast> forecasts) {
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
        if (precipitationBefore && precipitationAfter) {
            message = "Niederschlag auf Hin- und Rückfahrt erwartet. **Regenschirm einpacken!**";
        } else if (precipitationBefore) {
            message = "Niederschlag auf der Hinfahrt erwartet. **Regenschirm einpacken!**";
        } else if (precipitationAfter) {
            message = "Niederschlag auf der Rückfahrt erwartet. **Regenschirm einpacken!**";
        } else {
            message = "Kein Niederschlag auf der Fahrt erwartet. **Regenschirm nicht nötig.**";
        }

        log.info("Message is: {}", message);
        return message;
    }
}
