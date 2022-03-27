package ru.spbstu.ip;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import ru.spbstu.storage.ip.GeoIP;

public class IpEntry {

    private final long userId;
    private final GeoIP geoIPInfo;
    private boolean verified;

    public IpEntry(long userId,
                   @NotNull GeoIP geoIPInfo) {
        Validate.notNull(geoIPInfo);
        this.userId = userId;
        this.geoIPInfo = geoIPInfo;
    }

    public long getUserId() {
        return userId;
    }

    @NotNull
    public GeoIP getGeoIPInfo() {
        return geoIPInfo;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
