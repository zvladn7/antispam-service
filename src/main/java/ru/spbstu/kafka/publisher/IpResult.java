package ru.spbstu.kafka.publisher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpResult {

    private static final String USER_ID_FIELD = "user_id";
    private static final String IP_FIELD = "ip";
    private static final String VPN_FIELD = "vpn";
    private static final String ERROR_CODE_FIELD = "error_code";
    private static final String CORRELATION_FIELD = "correlation";
    private static final String IP_ACTIVITY_INFOS_FIELD = "ip_activity_infos";
    private static final String USER_ACTIVITY_INFOS_FIELD = "user_activity_infos";

    private final long userId;
    private final String ip;
    private final boolean vpn;
    private final ErrorCode errorCode;
    private final double correlation;
    private final List<ActivityInfoDTO> ipActivityInfos;
    private final List<ActivityInfoDTO> userActivityInfos;

    public IpResult(@JsonProperty(value = USER_ID_FIELD) long userId,
                    @JsonProperty(value = IP_FIELD) String ip,
                    @JsonProperty(value = VPN_FIELD) boolean vpn,
                    @JsonProperty(value = ERROR_CODE_FIELD) ErrorCode errorCode,
                    @JsonProperty(value = CORRELATION_FIELD) double correlation,
                    @JsonProperty(value = IP_ACTIVITY_INFOS_FIELD) List<ActivityInfoDTO> ipActivityInfos,
                    @JsonProperty(value = USER_ACTIVITY_INFOS_FIELD) List<ActivityInfoDTO> userActivityInfos) {
        this.userId = userId;
        this.ip = ip;
        this.vpn = vpn;
        this.errorCode = errorCode;
        this.correlation = correlation;
        this.ipActivityInfos = ipActivityInfos;
        this.userActivityInfos = userActivityInfos;
    }

    @JsonProperty(value = USER_ID_FIELD)
    public long getUserId() {
        return userId;
    }

    @JsonProperty(value = IP_FIELD)
    public String getIp() {
        return ip;
    }

    @JsonProperty(value = VPN_FIELD)
    public boolean isVpn() {
        return vpn;
    }

    @JsonProperty(value = ERROR_CODE_FIELD)
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @JsonProperty(value = CORRELATION_FIELD)
    public double getCorrelation() {
        return correlation;
    }

    @JsonProperty(value = IP_ACTIVITY_INFOS_FIELD)
    public List<ActivityInfoDTO> getIpActivityInfos() {
        return ipActivityInfos;
    }

    @JsonProperty(value = USER_ACTIVITY_INFOS_FIELD)
    public List<ActivityInfoDTO> getUserActivityInfos() {
        return userActivityInfos;
    }

}
