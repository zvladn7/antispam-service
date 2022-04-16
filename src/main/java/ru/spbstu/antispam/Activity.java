package ru.spbstu.antispam;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Activity {

    private static final Builder BUILDER = new Builder();

    public static final Activity IP_USERS_COUNT = BUILDER.addActivity("IP_USERS_COUNT", 1);
    public static final Activity IP_FIRST_TIME = BUILDER.addActivity("IP_FIRST_TIME", 2);
    public static final Activity IP_LAST_TIME = BUILDER.addActivity("IP_LAST_TIME", 3);

    public static final Activity USER_FIRST_TIME_LOGIN = BUILDER.addActivity("USER_FIRST_TIME_LOGIN", 4);
    public static final Activity USER_LAST_TIME_LOGIN = BUILDER.addActivity("USER_LAST_TIME_LOGIN", 5);
    public static final Activity USER_LAST_IP_HASH = BUILDER.addActivity("USER_LAST_IP", 6);

    //all messages limit
    public static final Activity MESSAGE_WINDOW_COUNT = BUILDER.addActivity("MESSAGE_WINDOW_COUNT", 7);
    public static final Activity MESSAGE_WINDOW_FIRST_TIMESTAMP = BUILDER.addActivity("MESSAGE_WINDOW_FIRST_TIMESTAMP", 8);
    public static final Activity MESSAGE_WINDOW_LAST_TIMESTAMP = BUILDER.addActivity("MESSAGE_WINDOW_LAST_TIMESTAMP", 7);

    private final String name;
    private final int id;

    public Activity(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    private static class Builder {

        private final Map<String, Activity> nameToActivity = new HashMap<>();

        public Activity addActivity(@NotNull String name, int value) {
            Activity activity = new Activity(name, value);
            nameToActivity.put(name, activity);
            return activity;
        }

        public Map<String, Activity> getNameToActivity() {
            return nameToActivity;
        }

    }

    @Nullable
    public static Activity getActivityByName(@NotNull String activityName) {
        return BUILDER.getNameToActivity().get(activityName);
    }

}
