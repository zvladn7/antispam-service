package ru.spbstu.antispam;

public class Activity {

    public static final Activity IP_USERS_COUNT = new Activity("IP_USERS_COUNT", 1);
    public static final Activity IP_FIRST_TIME = new Activity("IP_FIRST_TIME", 2);
    public static final Activity IP_LAST_TIME = new Activity("IP_LAST_TIME", 3);

    public static final Activity USER_FIRST_TIME_LOGIN = new Activity("USER_FIRST_TIME_LOGIN", 4);
    public static final Activity USER_LAST_TIME_LOGIN = new Activity("USER_LAST_TIME_LOGIN", 5);
    public static final Activity USER_LAST_IP_HASH = new Activity("USER_LAST_IP", 6);

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

}
