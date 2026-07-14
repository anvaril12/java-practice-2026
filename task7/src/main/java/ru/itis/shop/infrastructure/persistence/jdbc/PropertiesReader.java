package ru.itis.shop.infrastructure.persistence.jdbc;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = PropertiesReader.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties не найден");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("ошибка загрузки application.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}