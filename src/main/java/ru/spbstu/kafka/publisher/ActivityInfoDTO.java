package ru.spbstu.kafka.publisher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityInfoDTO {

    private static final String NAME_FIELD = "name";
    private static final String VALUE_FIELD = "value";

    private final String name;
    private final int value;

    public ActivityInfoDTO(@JsonProperty(value = NAME_FIELD, required = true) String name,
                           @JsonProperty(value = VALUE_FIELD, required = true) int value) {
        this.name = name;
        this.value = value;
    }

    @NotNull
    @JsonProperty(value = NAME_FIELD)
    public String getName() {
        return name;
    }

    @JsonProperty(value = VALUE_FIELD)
    public int getValue() {
        return value;
    }

}
