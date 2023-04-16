// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package ru.spbstu.messages;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.spbstu.antispam.Activity;
import ru.spbstu.antispam.ActivityInfo;
import ru.spbstu.antispam.SpamDocument;
import ru.spbstu.kafka.publisher.KafkaPublisher;
import ru.spbstu.kafka.publisher.MessageResult;
import ru.spbstu.storage.postgres.StorageService;
import ru.spbstu.storage.postgres.dto.UserInfo;
import ru.spbstu.util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessagesService {

    private static final int DAY_PER_USER_THRESHOLD = 5;

    private final KafkaPublisher<MessageResult> messageResultKafkaPublisher;
    private final StorageService storageService;

    public MessagesService(@NotNull KafkaPublisher<MessageResult> messageResultKafkaPublisher,
                           @NotNull StorageService storageService) {
        Validate.notNull(messageResultKafkaPublisher);
        Validate.notNull(storageService);

        this.messageResultKafkaPublisher = messageResultKafkaPublisher;
        this.storageService = storageService;
    }

    public void processMessage(@NotNull SpamDocument document) {
        List<ActivityInfo> userActivities = storageService.getUserActivities(document.getSenderId());
        Integer firstTime = getCurrentState(Activity.MESSAGE_WINDOW_FIRST_TIMESTAMP, userActivities);
        Integer lastTime = getCurrentState(Activity.MESSAGE_WINDOW_LAST_TIMESTAMP, userActivities);
        Integer count = getCurrentState(Activity.MESSAGE_WINDOW_COUNT, userActivities);
        int currentTime = DateUtil.currentDateCompact();
        int dayStartSeconds = LocalDate.now().atStartOfDay().getSecond();
        if (firstTime == null || (lastTime != null && dayStartSeconds > lastTime)) {
            update(document.getSenderId(), currentTime, currentTime, 1);
            return;
        }
        int newCount = count == null ? 1 : count + 1;
        update(document.getSenderId(), firstTime, currentTime, newCount);
        if (DAY_PER_USER_THRESHOLD <= newCount) {
            messageResultKafkaPublisher.publish(new MessageResult(
                    document.getSenderId(),
                    document.getMessageType(),
                    newCount
            ));
        }
    }

    @Nullable
    private Integer getCurrentState(Activity activity,
                                    List<ActivityInfo> userActivities) {
        if (!CollectionUtils.isEmpty(userActivities)) {
            for (ActivityInfo activityInfo : userActivities) {
                if (activity.getName().equals(activityInfo.getActivity().getName())) {
                    return activityInfo.getValue();
                }
            }
        }
        return null;
    }

    private void update(long userId,
                        int firstTime,
                        int lastTime,
                        int count) {
        List<ActivityInfo> newUserActivitiesInfo = new ArrayList<>();
        newUserActivitiesInfo.add(new ActivityInfo(Activity.MESSAGE_WINDOW_FIRST_TIMESTAMP, firstTime));
        newUserActivitiesInfo.add(new ActivityInfo(Activity.MESSAGE_WINDOW_LAST_TIMESTAMP, lastTime));
        newUserActivitiesInfo.add(new ActivityInfo(Activity.MESSAGE_WINDOW_COUNT, count));
        storageService.saveUserInfo(new UserInfo(
                userId,
                newUserActivitiesInfo
        ));
    }


}
