import Config.ProjectConfig;
import Converters.Temperature;
import Requests.WeatherRequests;
import Types.TemperatureType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

import static org.testng.Assert.assertEquals;

public class Weather {
    private String apiKey;
    private LocalDate date;

    @DataProvider
    public Object[][] getCity() {
        return new Object[][]{
                new Object[] {"Yoshkar-Ola"}
        };
    }

    private void assertStatusCode(Response responseCelsius, Response responseFahrenheit) {
        assertEquals(responseCelsius.getStatusCode(), HttpStatus.SC_OK);
        assertEquals(responseFahrenheit.getStatusCode(), HttpStatus.SC_OK);
    }

    private void assertTemperatureConverter(Response responseCelsius, Response responseFahrenheit, LocalDate subtractionDate) {
        JsonPath jsonCelsius = responseCelsius.jsonPath();
        JsonPath jsonFahrenheit = responseFahrenheit.jsonPath();

        Temperature convertedTemp;
        float siteFahrenheitTemp;
        try {
            //Today
            convertedTemp = new Temperature(jsonCelsius.getFloat("main.temp"), TemperatureType.CELSIUS);
            siteFahrenheitTemp = jsonFahrenheit.getFloat("main.temp");
        }
        catch (NullPointerException e) {
            //Future
            int substraction = Period.between(LocalDate.now(), subtractionDate).getDays();
            try {
                convertedTemp = new Temperature(jsonCelsius.getFloat("list." + substraction + ".temp"), TemperatureType.CELSIUS);
                siteFahrenheitTemp = jsonFahrenheit.getFloat("list." + substraction + ".temp");
            }
            catch (NullPointerException ex) {
                //Past
                convertedTemp = new Temperature(jsonCelsius.getFloat("list.main.temp"), TemperatureType.CELSIUS);
                siteFahrenheitTemp = jsonFahrenheit.getFloat("list.main.temp");
            }
        }
        assertEquals(siteFahrenheitTemp, Math.round(convertedTemp.getConvertedTemperature()));
    }

    private void assertThis(Response responseCelsius, Response responseFahrenheit, LocalDate subtractionDate) {
        assertStatusCode(responseCelsius, responseFahrenheit);
        assertTemperatureConverter(responseCelsius, responseFahrenheit, subtractionDate);
    }

    @BeforeTest
    public void SetUp() throws IOException {
        ProjectConfig config = new ProjectConfig();
        apiKey = config.getApiKey();
        date = config.getDate();
    }

    @Test(dataProvider="getCity")
    public void TestCompareTemperature(String city) throws Exception {
        //Arrange
        WeatherRequests requests = new WeatherRequests(apiKey, date);

        //Act
        Response jsonResponseCelsius = requests.GetTemperatureInCity(city, TemperatureType.CELSIUS);
        Response jsonResponseFahrenheit = requests.GetTemperatureInCity(city, TemperatureType.FAHRENHEIT);

        //Assert
        assertThis(jsonResponseCelsius, jsonResponseFahrenheit, date);
    }
}
