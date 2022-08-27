package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Devdun.k
 */

public class ConfigFileReader {
    private Properties properties;
    private final String propertyFilePath= System.getProperty("user.dir")+"\\src\\main\\resources\\system.properties";

    public ConfigFileReader(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    public String applicationUrl_QA() {
        String applicationUrl = properties.getProperty("applicationUrl_QA");
        if(applicationUrl != null) return applicationUrl;
        else throw new RuntimeException("url not specified in the Configuration.properties file.");
    }

}
