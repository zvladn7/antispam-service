package ru.spbstu.storage.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.spbstu.antispam.ActivityInfo;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "userInfo")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT", name = "userActivities")
    private List<ActivityInfo> userActivities;

    @OneToOne(mappedBy = "userId")
    private IpEntryListDTO ipEntryListDTO;

}
