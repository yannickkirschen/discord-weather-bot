package sh.yannick.bot.weather.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.core5.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.*;
import java.util.*;

/**
 * Provides access to Open Meteo for downloading weather data.
 * <p>
 * This bean requires a property <code>weather.url</code> pointing to the Open Meteo API including all properties
 * needed. The URL especially needs one or more <code>DATE_PLACEHOLDER</code>s that will be replaced by the given date
 * before executing the request.
 *
 * @author Yannick Kirschen
 * @since 1.0.0
 */
@Slf4j
@Repository
public class WeatherForecastProviderOpenMeteo implements WeatherForecastProvider {
    @Value("${weather.url}")
    private String url;

    @Override
    public List<Forecast> retrieve(LocalDate date) {
        MeteoResponse response = getData(url.replaceAll("DATE_PLACEHOLDER", date.toString()));

        Iterator<Double> rainIterator = response.getHourly().getRain().iterator();
        Iterator<Double> showersIterator = response.getHourly().getShowers().iterator();
        Iterator<Double> snowfallIterator = response.getHourly().getSnowfall().iterator();

        List<Forecast> forecasts = new LinkedList<>();
        for (LocalDateTime dateTime : response.getHourly().getTime()) {
            double precipitation = rainIterator.next() + showersIterator.next() + snowfallIterator.next();

            Forecast f = new Forecast();
            f.setDateTime(dateTime);
            f.setPrecipitation(precipitation != 0);

            forecasts.add(f);

            log.info("Precipitation for {}: {}mm.", dateTime, precipitation);
        }

        return forecasts;
    }

    private MeteoResponse getData(String url) {
        log.info("Performing HTTP request to Open Meteo: {}", url);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            ClassicHttpResponse response = client.execute(get);

            int status = response.getCode();
            if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
                log.info("Received data from Open Meteo.");

                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();

                ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
                return mapper.readValue(in, MeteoResponse.class);
            } else {
                log.error("Bad response from Open Meteo servers. Status: {}", status);
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (IOException e) {
            log.error("Error while calling Open Meteo servers: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MeteoResponse {
        private double latitude;
        private double longitude;
        private String timezone;
        private MeteoHourly hourly;
    }

    @Data
    private static class MeteoHourly {
        private List<LocalDateTime> time;
        private List<Double> rain;
        private List<Double> showers;
        private List<Double> snowfall;
    }
}
