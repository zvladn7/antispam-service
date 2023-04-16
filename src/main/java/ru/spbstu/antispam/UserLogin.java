// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.antispam;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import ru.spbstu.kafka.user.UserLoginDTO;

public class UserLogin {

    private final long userId;
    private final String loginIpAddress;
    private final long loginTime;

    public UserLogin(@NotNull UserLoginDTO userLoginDTO) {
        Validate.notNull(userLoginDTO);

        this.userId = userLoginDTO.getUserId();
        this.loginIpAddress = userLoginDTO.getLoginIpAddress();
        this.loginTime = userLoginDTO.getLoginTime();
    }

    public long getUserId() {
        return userId;
    }

    public String getLoginIpAddress() {
        return loginIpAddress;
    }

    public long getLoginTime() {
        return loginTime;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "userId=" + userId +
                ", loginIpAddress='" + loginIpAddress + '\'' +
                ", loginTime=" + loginTime +
                '}';
    }

}
