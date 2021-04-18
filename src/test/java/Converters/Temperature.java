package Converters;

import Requests.WeatherRequests;
import Types.TemperatureType;

import java.security.PublicKey;

public class Temperature {
    public float getCurrTemperature() {
        return currTemperature;
    }

    private float currTemperature;

    public float getConvertedTemperature() {
        return convertedTemperature;
    }

    private float convertedTemperature;

    private void ConvertTemperatureToFahrenheit() {
        convertedTemperature = ((currTemperature * 9)/5) + 32;
    }

    private void ConvertTemperatureToCelsius() {
        convertedTemperature = ((currTemperature-32)*5)/9;
    }

    public Temperature(Float temperature, @org.jetbrains.annotations.NotNull TemperatureType temperatureType) {
        currTemperature = temperature;
        switch (temperatureType) {
            case CELSIUS:
                ConvertTemperatureToFahrenheit();
                break;
            case FAHRENHEIT:
                ConvertTemperatureToCelsius();
                break;
        }

    }

}
