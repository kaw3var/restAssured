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

    /**
     * Получить значение свойства по ключу
     * @param key ключ свойства
     * @return значение свойства
     * @throws RuntimeException если ключ не найден
     */
    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Свойство '" + key + "' не найдено в application.properties");
        }
        return value;
    }

    /**
     * Получить значение свойства по ключу с дефолтным значением
     * @param key ключ свойства
     * @param defaultValue значение по умолчанию, если ключ не найден
     * @return значение свойства или defaultValue
     */
    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    /**
     * Получить значение как Integer
     * @param key ключ свойства
     * @return значение как Integer
     */
    public static Integer getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Свойство '" + key + "' не является числом: " + value);
        }
    }

    /**
     * Получить значение как Integer с дефолтным значением
     * @param key ключ свойства
     * @param defaultValue значение по умолчанию
     * @return значение как Integer или defaultValue
     */
    public static Integer getInt(String key, Integer defaultValue) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Получить значение как Boolean
     * @param key ключ свойства
     * @return значение как Boolean
     */
    public static Boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    /**
     * Получить значение как Boolean с дефолтным значением
     * @param key ключ свойства
     * @param defaultValue значение по умолчанию
     * @return значение как Boolean или defaultValue
     */
    public static Boolean getBoolean(String key, Boolean defaultValue) {
        String value = PROPERTIES.getProperty(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    /**
     * Получить все свойства
     * @return Properties объект
     */
    public static Properties getAllProperties() {
        return PROPERTIES;
    }

    /**
     * Проверить, существует ли свойство
     * @param key ключ свойства
     * @return true если свойство существует
     */
    public static boolean contains(String key) {
        return PROPERTIES.containsKey(key);
    }
}
