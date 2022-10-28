package sh.yannick.bot.weather;

import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.core5.http.*;
import org.springframework.stereotype.Repository;
import sh.yannick.bot.weather.model.LectureDay;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Repository
public class LectureDayProviderICAL implements LectureDayProvider {
    @Override
    public LectureDay retrieve(LocalDate date, String url) {
        Calendar calendar = getCalendar(url);

        List<CalendarComponent> components = calendar.getComponents().stream().filter(c -> {
            Property start = c.getProperty("DTSTART");
            if (start != null) {
                LocalDateTime dateTime = parseDateTime(start.getValue());
                return dateTime.toLocalDate().equals(date);
            }
            return false;
        }).toList();

        Optional<LocalDateTime> earliestBegin = components.stream().map(c -> parseDateTime(c.getProperty("DTSTART").getValue())).sorted().findFirst();
        Optional<LocalDateTime> latestEnd = components.stream().map(c -> parseDateTime(c.getProperty("DTEND").getValue())).max(Comparator.naturalOrder());

        if (earliestBegin.isEmpty()) {
            log.warn("There are no lectures for today ({}).", date);
            throw new RuntimeException("No lectures today");
        }

        LectureDay day = new LectureDay();
        day.setBegin(earliestBegin.get());
        day.setEnd(latestEnd.get());

        log.info("Lecture day begins at {} and ends at {}.", day.getBegin(), day.getEnd());
        return day;
    }

    private LocalDateTime parseDateTime(String value) {
        String[] dateTime = value.split("T");
        LocalDate date = LocalDate.parse(dateTime[0], DateTimeFormatter.BASIC_ISO_DATE);

        int hour = Integer.parseInt(dateTime[1].substring(0, 2));
        int minute = Integer.parseInt(dateTime[1].substring(2, 4));
        int second = Integer.parseInt(dateTime[1].substring(4, 6));

        return LocalDateTime.of(date, LocalTime.of(hour, minute, second));
    }

    private Calendar getCalendar(String url) {
        log.info("Downloading ICS calendar file: {}", url);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            ClassicHttpResponse response = client.execute(get);

            int status = response.getCode();
            if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                log.info("Received ICS file.");

                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                CalendarBuilder builder = new CalendarBuilder();
                try {
                    return builder.build(in);
                } catch (ParserException e) {
                    log.error("Unable to parse ICS file: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            } else {
                log.error("Bad response from server while downloading ICS file. Status: {}", status);
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (IOException e) {
            log.error("Error while downloading ICS file: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
