package ru.spbstu.storage.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table
public class IpEntryDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", updatable = false)
    private long userId;

    @Column(
            name = "ip_address",
            updatable = false,
            nullable = false,
            length = 50
    )
    private String ipAddress;

    @Column(
            name = "city",
            updatable = false,
            length = 50
    )
    private String city;

    @Column(
            name = "latitude",
            nullable = false,
            updatable = false
    )
    private double latitude;

    @Column(
            name = "longitude",
            nullable = false,
            updatable = false
    )
    private double longitude;

    @Column(
            name = "first_time",
            nullable = false,
            updatable = false
    )
    private int firstTime;

    @Column(
            name = "last_time",
            nullable = false
    )
    private int lastTime;

    @Column(
            name = "verified",
            nullable = false,
            updatable = false
    )
    private boolean verified;


    public IpEntryDTO(long userId,
                      String ipAddress,
                      String city,
                      double latitude,
                      double longitude,
                      int firstTime,
                      int lastTime,
                      boolean verified) {
        Validate.notNull(ipAddress);
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.verified = verified;
    }

}
