package sh.yannick.bot.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.core5.http.*;
import org.springframework.stereotype.Repository;
import sh.yannick.bot.weather.model.*;

import java.io.*;
import java.time.*;
import java.util.*;

@Slf4j
@Repository
public class WeatherForecastProviderOpenMeteo implements WeatherForecastProvider {
    @Override
    public List<Forecast> retrieve(LocalDate date, String url) {
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
}
