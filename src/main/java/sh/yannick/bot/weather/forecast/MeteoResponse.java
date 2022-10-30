package sh.yannick.bot.weather.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeteoResponse {
    private double latitude;
    private double longitude;
    private String timezone;
    private MeteoHourly hourly;
}
