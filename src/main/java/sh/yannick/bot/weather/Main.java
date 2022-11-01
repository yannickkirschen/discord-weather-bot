package sh.yannick.bot.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ((WeatherBot) SpringApplication.run(Main.class, args).getBean("weatherBot")).run();
    }
}
