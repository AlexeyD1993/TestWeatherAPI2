package Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

public class ProjectConfig {
    private Properties locators;

    public String getApiKey() {
        return apiKey;
    }

    private String apiKey;

    public LocalDate getDate() {
        return date;
    }

    private LocalDate date;

    private void LoadConfig() throws IOException {
        locators = new Properties();
        String propFileName = "project.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            locators.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
    }

    public ProjectConfig() throws IOException {
        LoadConfig();

        apiKey = locators.getProperty("apiKey");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date = LocalDate.parse(locators.getProperty("date"), dtf);
    }
}
