// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.ip;

import org.jetbrains.annotations.NotNull;
import ru.spbstu.storage.ip.GeoIP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreparedIpEntryFactory {

    @NotNull
    public static PreparedIpEntry prepare(@NotNull IpEntry ipEntry) {
        return prepare(Collections.singletonList(ipEntry)).get(0);
    }

    @NotNull
    public static List<PreparedIpEntry> prepare(List<IpEntry> ipEntries) {
        List<PreparedIpEntry> preparedIpEntries = new ArrayList<>();
        for (IpEntry entry : ipEntries) {
            GeoIpFeatures geoIpFeatures = provideGeoIpFeatures(entry);
            preparedIpEntries.add(new PreparedIpEntry(entry, geoIpFeatures));
        }
        return preparedIpEntries;
    }

    private static GeoIpFeatures provideGeoIpFeatures(@NotNull IpEntry entry) {
        GeoIpFeatures geoIpFeatures = new GeoIpFeatures();
        GeoIP geoIPInfo = entry.getGeoIPInfo();
        double latitude = geoIPInfo.getLatitude();
        double longitude = geoIPInfo.getLongitude();
        geoIpFeatures.setCoordinates(CoordinatesUtil.getCoordinatesAsString(latitude, longitude));
        return geoIpFeatures;
    }

}
