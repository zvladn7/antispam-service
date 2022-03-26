package ru.spbstu.storage.ip;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class GeoIP {

    private final String ipAddress;
    private final String city;
    private final double latitude;
    private final double longitude;

}
