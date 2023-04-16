// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
