// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesProvider {

    private PropertiesProvider() {
    }

    private static final Logger log = LoggerFactory.getLogger(PropertiesProvider.class);

    @NotNull
    public static Properties provideProperties(@NotNull String propertiesFileName) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propertiesFileName))) {
            Properties properties = new Properties();
            properties.load(bis);
            return properties;
        } catch (IOException e) {
            log.error("Failed to read properties file", e);
            return new Properties();
        }
    }

}
