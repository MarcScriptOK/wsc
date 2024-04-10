package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyFileReader {
    public static Properties properties;

    /**
     * Path of the config execution property file
     */
    private static final String propertyFilePathConfig = "config.properties";

    /**
     * Method that return a value from config.properties file.
     * @param propertyKey String key of the property.
     * @return String value of the property.
     */
    public static  String getPropertyConfig(String propertyKey){
        loadReader(propertyFilePathConfig);
        String propertyValue = properties.getProperty(propertyKey);
        if(propertyValue != null) return propertyValue;
        else throw new RuntimeException("Property not specified in the config.properties file.");
    }

    /**
     * Method that loads the path of the corresponding property file.
     * @param propertyFilePath String path of the property file in project.
     */
    public static void loadReader(String propertyFilePath) {
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
            throw new RuntimeException("Property file not found at " + propertyFilePath);
        }
    }
}