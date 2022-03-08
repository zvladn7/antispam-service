package ru.spbstu.kafka.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginDTO {

    private static final String USER_ID = "user_id";
    private static final String LOGIN_IP_ADDRESS = "login_ip_address";
    private static final String LOGIN_TIME = "login_time";

    private final long userId;
    private final String loginIpAddress;
    private final long loginTime;

    public UserLoginDTO(@JsonProperty(value = USER_ID, required = true) long userId,
                        @JsonProperty(value = LOGIN_IP_ADDRESS, required = true) String loginIpAddress,
                        @JsonProperty(value = LOGIN_TIME, required = true) long loginTime) {
        this.userId = userId;
        this.loginIpAddress = loginIpAddress;
        this.loginTime = loginTime;
    }

    @JsonProperty(value = USER_ID)
    public long getUserId() {
        return userId;
    }

    @JsonProperty(value = LOGIN_IP_ADDRESS)
    public String getLoginIpAddress() {
        return loginIpAddress;
    }

    @JsonProperty(value = LOGIN_TIME)
    public long getLoginTime() {
        return loginTime;
    }

}
