package ru.spbstu.storage.ip;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class GeoIpDAO implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(GeoIpDAO.class);
    private static final String DB_FILE_LOCATION = "src/main/resources/GeoLite2-City.mmdb";

    private volatile boolean running;
    private DatabaseReader dbReader;

    @Nullable
    public GeoIP getLocation(@NotNull String ip) {
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            logger.warn("Ip address for this host couldn't be found, ip: [{}]", ip, e);
            return null;
        }
        CityResponse cityResponse;
        try {
            cityResponse = dbReader.city(ipAddress);
        } catch (IOException | GeoIp2Exception e) {
            logger.warn("Failed to get information from GeoIpDAO for ip [{}]", ip, e);
            return null;
        }
        String cityName = cityResponse.getCity().getName();
        double latitude = cityResponse.getLocation().getLatitude();
        double longitude = cityResponse.getLocation().getLongitude();
        return new GeoIP(ip, cityName, latitude, longitude);
    }

    @Override
    public void start() {
        File database = new File(DB_FILE_LOCATION);
        try {
            dbReader = new DatabaseReader.Builder(database).build();
        } catch (Exception e) {
            logger.error("Failed to open geo ip database reader, file: [{}]", database.getAbsolutePath(), e);
        }
        running = true;
        logger.info("normally started");
    }

    @Override
    public void stop() {
        try {
            dbReader.close();
        } catch (IOException e) {
            logger.error("Failed to close GeoIPDAO reader", e);
        }
        running = false;
        logger.info("normally started");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        try {
            callback.run();
        } finally {
            stop();
        }
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

}
