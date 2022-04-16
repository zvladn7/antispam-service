package ru.spbstu.storage.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import ru.spbstu.antispam.ActivityInfo;
import ru.spbstu.kafka.publisher.ActivityInfoDTO;
import ru.spbstu.kafka.publisher.ActivityInfoMapper;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
@Table
public class UserInfo {

    @Id
    @Getter
    private long userId;

    @Column(name = "userActivities")
    private String userActivities;

    public UserInfo(long userId,
                    List<ActivityInfo> userActivities) {
        this.userId = userId;
        this.userActivities = ActivityInfoMapper.convertToString(userActivities);
    }

    @NotNull
    public List<ActivityInfo> getUserActivities() {
        return ActivityInfoMapper.convertFromString(userActivities);
    }

}
