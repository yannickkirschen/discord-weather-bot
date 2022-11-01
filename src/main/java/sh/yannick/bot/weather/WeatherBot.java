package sh.yannick.bot.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sh.yannick.bot.weather.calendar.*;
import sh.yannick.bot.weather.forecast.*;
import sh.yannick.bot.weather.message.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Weather bot recommending to pack your umbrella or not.
 * <p>
 * Basically, this bot works as follows:
 * <ol>
 *     <li>Get today's lecture plan ({@link LectureDayProvider})</li>
 *     <li>Get today's weather forecast ({@link WeatherForecastProvider})</li>
 *     <li>Check, if there is precipitation expected:
 *     <ul>
 *         <li>two hours before the first lecture until the beginning of the first lecture</li>
 *         <li>from the end of the last lecture until two hours later</li>
 *     </ul></li>
 *     <li>Generate a localized recommendation message ({@link MessageGenerator})</li>
 *     <li>Send the message to a Discord channel ({@link MessageSender})</li>
 * </ol>
 *
 * @author Yannick Kirschen
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherBot implements Runnable {
    private final LectureDayProvider lecturePlanProvider;
    private final WeatherForecastProvider weatherProvider;
    private final MessageSender sender;

    private final MessageGenerator generator;

    @Value("${messages.locale}")
    private String locale;

    @Override
    public void run() {
        LocalDate date = LocalDate.now();
        log.info("Generating forecast for {}.", date);

        try {
            LectureDay lectureDay = lecturePlanProvider.retrieve(date);
            List<Forecast> forecasts = weatherProvider.retrieve(date);

            String message = generator.generate(lectureDay, forecasts, new Locale(locale));
            sender.send(message);
        } catch (NoLecturesFoundException e) {
            log.error("There are no lectures for today ({}).", date);
        }
    }
}
