package ru.spbstu.kafka.publisher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpResult {

    private static final String USER_ID_FIELD = "user_id";
    private static final String CORRELATION_FIELD = "correlation";
    private static final String ACTIVITY_INFOS_FIELD = "activity_infos";

    private final long userId;
    private final double correlation;
    private final List<ActivityInfoDTO> activityInfos;

    public IpResult(@JsonProperty(value = USER_ID_FIELD) long userId,
                    @JsonProperty(value = CORRELATION_FIELD) double correlation,
                    @JsonProperty(value = ACTIVITY_INFOS_FIELD) List<ActivityInfoDTO> activityInfos) {
        this.userId = userId;
        this.correlation = correlation;
        this.activityInfos = activityInfos;
    }

    @JsonProperty(value = USER_ID_FIELD)
    public long getUserId() {
        return userId;
    }

    @JsonProperty(value = CORRELATION_FIELD)
    public double getCorrelation() {
        return correlation;
    }

    @JsonProperty(value = ACTIVITY_INFOS_FIELD)
    public List<ActivityInfoDTO> getActivityInfos() {
        return activityInfos;
    }

}
