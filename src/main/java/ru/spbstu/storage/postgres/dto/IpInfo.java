package ru.spbstu.storage.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.spbstu.antispam.ActivityInfo;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "ipinfo")
public class IpInfo {

    @Id
    private String ip;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT", name = "ipActivities")
    private List<ActivityInfo> ipActivities;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT", name = "users")
    private Set<String> users;

}
