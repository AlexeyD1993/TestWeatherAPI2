package Types;
public enum TemperatureType {
    CELSIUS ("metric"),
    FAHRENHEIT("imperial");

    private String type;

    TemperatureType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
