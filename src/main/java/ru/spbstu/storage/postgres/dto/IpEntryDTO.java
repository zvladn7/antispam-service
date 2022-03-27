package ru.spbstu.storage.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@Table(name = "IpEntry")
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
            nullable = false,
            updatable = false,
            length = 50
    )
    private String city;

    @Column(
            name = "city",
            nullable = false,
            updatable = false
    )
    private double latitude;

    @Column(
            name = "city",
            nullable = false,
            updatable = false
    )
    private double longitude;

    @Column(
            name = "city",
            nullable = false,
            updatable = false
    )
    private boolean verified;

}
