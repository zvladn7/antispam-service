package ru.spbstu.vpn;

import com.google.gson.Gson;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public final class VPNDetection {

    private static final String API_URL = "http://api.vpnblocker.net/v2/json/";
    private static final int TIMEOUT = 5000;

    private String apiKey;

    public VPNDetection() {
        this.apiKey = null;
    }

    /**
     * API key from: https://vpnblocker.net
     */
    public void setApiKey(String key) {
        this.apiKey = key;
    }

    public Response getResponse(String ip) throws IOException {
        String queryUrl = this.getQuery(ip);
        String queryResult = this.query(queryUrl, "Java-VPNDetection Library");
        return new Gson().fromJson(queryResult, Response.class);
    }

    public String getQuery(String ip) {
        return (this.apiKey == null) ? API_URL + ip : API_URL + ip + "/" + this.apiKey;
    }

    public String query(@NotNull String url,
                        @NotNull String userAgent) throws IOException {
        Validate.notNull(url);
        Validate.notNull(userAgent);
        StringBuilder response = new StringBuilder();
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.setConnectTimeout(TIMEOUT);
        connection.setRequestProperty("User-Agent", userAgent);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            while ((url = in.readLine()) != null) {
                response.append(url);
            }
        }
        return response.toString();
    }
}
