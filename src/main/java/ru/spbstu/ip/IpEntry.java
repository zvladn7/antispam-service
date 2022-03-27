package ru.spbstu.ip;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import ru.spbstu.storage.ip.GeoIP;
import ru.spbstu.util.DateUtil;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class IpEntry {

    private final long userId;
    private final GeoIP geoIPInfo;
    private final int firstTime;
    private final int lastTime;
    @Setter
    private boolean verified;

    public IpEntry(long userId,
                   @NotNull GeoIP geoIPInfo) {
        Validate.notNull(geoIPInfo);
        this.userId = userId;
        this.geoIPInfo = geoIPInfo;
        this.firstTime = DateUtil.currentDateCompact();
        this.lastTime = firstTime;
    }

}
