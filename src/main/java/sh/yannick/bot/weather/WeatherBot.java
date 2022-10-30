package sh.yannick.bot.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sh.yannick.bot.weather.calendar.*;
import sh.yannick.bot.weather.forecast.*;
import sh.yannick.bot.weather.message.MessageSender;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherBot {
    private final LectureDayProvider lecturePlanProvider;
    private final WeatherForecastProvider weatherProvider;
    private final MessageSender sender;

    private final MessageGenerator generator;

    @Value("${calendar.url}")
    private String calendarUrl;

    @Value("${weather.url}")
    private String weatherUrl;

    public void execute() {
        LocalDate date = LocalDate.now();
        log.info("Generating forecast for {}.", date);

        LectureDay lectureDay = lecturePlanProvider.retrieve(date, calendarUrl);
        List<Forecast> forecasts = weatherProvider.retrieve(date, weatherUrl);

        String message = generator.generate(lectureDay, forecasts);
        sender.send(message);
    }
}
