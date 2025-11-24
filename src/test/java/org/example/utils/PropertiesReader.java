package org.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input =
                     PropertiesReader.class.getClassLoader().getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("Не найден application.properties");
            }

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения application.properties", e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
