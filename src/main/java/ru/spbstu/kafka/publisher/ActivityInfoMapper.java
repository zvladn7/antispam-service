package ru.spbstu.kafka.publisher;

import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbstu.antispam.ActivityInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityInfoMapper {

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

    private static ActivityInfoDTO convert(ActivityInfo activityInfo) {
        return new ActivityInfoDTO(activityInfo.getActivity().getName(), activityInfo.getValue());
    }

}
