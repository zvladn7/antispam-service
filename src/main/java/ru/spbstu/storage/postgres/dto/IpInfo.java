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
import org.jetbrains.annotations.Nullable;
import ru.spbstu.antispam.ActivityInfo;
import ru.spbstu.kafka.publisher.ActivityInfoMapper;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@ToString
@Table
public class IpInfo {

    @Id
    @Getter
    private String ip;

    @Column(name = "ipActivities")
    private String ipActivities;

    @Column(name = "users")
    private String users;

    public IpInfo(@NotNull String ip,
                  @NotNull List<ActivityInfo> ipActivities,
                  @NotNull List<Long> users) {
        this.ip = ip;
        this.ipActivities = ActivityInfoMapper.convertToString(ipActivities);
        this.users = users.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @NotNull
    public List<ActivityInfo> getIpActivities() {
        return ActivityInfoMapper.convertFromString(ipActivities);
    }

    @NotNull
    public List<Long> getUsers() {
        return Arrays.stream(users.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

}
