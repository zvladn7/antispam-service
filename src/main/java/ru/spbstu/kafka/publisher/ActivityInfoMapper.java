// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.kafka.publisher;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.CollectionUtils;
import ru.spbstu.antispam.Activity;
import ru.spbstu.antispam.ActivityInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityInfoMapper {

    private ActivityInfoMapper() {
    }

    @NotNull
    public static List<ActivityInfoDTO> convert(@Nullable List<ActivityInfo> activityInfos) {
        if (CollectionUtils.isEmpty(activityInfos)) {
            return Collections.emptyList();
        }
        List<ActivityInfoDTO> dtoList = new ArrayList<>();
        for (ActivityInfo activityInfo : activityInfos) {
            dtoList.add(convert(activityInfo));
        }
        return dtoList;
    }

    @NotNull
    public static String convertToString(@Nullable List<ActivityInfo> activityInfos) {
        if (CollectionUtils.isEmpty(activityInfos)) {
            return "null";
        }
        StringBuilder convertedActivities = new StringBuilder();
        for (ActivityInfo activityInfo : activityInfos) {
            convertedActivities.append(activityInfo.getActivity().getName())
                    .append("=")
                    .append(activityInfo.getValue())
                    .append("\n");
        }
        return convertedActivities.toString();
    }

    @NotNull
    public static List<ActivityInfo> convertFromString(@Nullable String activityInfosString) {
        if (activityInfosString == null || "null".equals(activityInfosString)) {
            return Collections.emptyList();
        }
        List<ActivityInfo> activityInfos = new ArrayList<>();
        String[] splittedActivities = activityInfosString.split("\n");
        for (String activityString : splittedActivities) {
            if (StringUtils.isBlank(activityString)) {
                continue;
            }
            String[] keyValue = activityString.split("=");
            activityInfos.add(new ActivityInfo(
                    Activity.getActivityByName(keyValue[0]),
                    Integer.parseInt(keyValue[1])
            ));
        }
        return activityInfos;
    }

    private static ActivityInfoDTO convert(ActivityInfo activityInfo) {
        return new ActivityInfoDTO(activityInfo.getActivity().getName(), activityInfo.getValue());
    }

}
