package ru.spbstu.kafka.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesProvider {

    private static final Logger log = LoggerFactory.getLogger(PropertiesProvider.class);

    public static Properties provideProperties(@NotNull String propertiesFileName) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("./messages-consumer-properties.p"))) {
            Properties properties = new Properties();
            properties.load(bis);
            return properties;
        } catch (FileNotFoundException e) {
            log.error("Failed to find file", e);
            return new Properties();
        } catch (IOException e) {
            log.error("Failed to read properties file for messages consumer", e);
            return new Properties();
        }
    }

}
