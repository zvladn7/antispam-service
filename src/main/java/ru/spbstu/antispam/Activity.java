package ru.spbstu.antispam;

public class Activity {

    private static final Activity USER_LAST_IP = new Activity("USER_LAST_IP", 1);

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
