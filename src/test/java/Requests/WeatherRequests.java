package Requests;

import Config.ProjectConfig;
import Converters.Temperature;
import Types.TemperatureType;
import groovyjarjarpicocli.CommandLine;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import static io.restassured.RestAssured.get;

public class WeatherRequests {
    private String apiKey;
    private LocalDate date;

    public WeatherRequests(String apiKey, LocalDate date) throws IOException {
        this.apiKey = apiKey;
        this.date = date;
    }

    public Response GetTemperatureInCity(String city, TemperatureType temperature) throws Exception {
        String urlRequest = "";
        if (date.isEqual(LocalDate.now())) {
            urlRequest = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + this.apiKey + "&units=" + temperature.getType();
        }
        else if (date.isAfter(LocalDate.now())) {
            Period period = Period.between ( LocalDate.now() , date );;
            int subtractionDate = period.getDays();
            if (subtractionDate > 16) throw new Exception("Max count of days may be only current + 16 days");
            urlRequest = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&cnt=" + subtractionDate + "&appid=" + this.apiKey + "&units=" + temperature.getType();
        }
        else if (date.isBefore(LocalDate.now())) {
            ZoneId zone = ZoneId.systemDefault();
            long startDateUnixTime = date.atStartOfDay(zone).toEpochSecond();
            long endDateUnixTime = date.plusDays(1).atStartOfDay(zone).toEpochSecond() - 1;

            urlRequest = "http://history.openweathermap.org/data/2.5/history/city?q=" + city + "&type=day&start=" + startDateUnixTime +
                    "&end=" + endDateUnixTime + "&appid=" + this.apiKey + "&units=" + temperature.getType();
        }
        else
            return null;
        return get(urlRequest);
    }
}
