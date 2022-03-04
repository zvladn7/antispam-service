package ru.spbstu.antispam;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ActivityInfo implements Serializable {

    private final Activity activity;
    private final int value;

}
