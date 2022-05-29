package bdtc.lab2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SensorType {
    TEMP("temp"),
    HUM("hum"),
    PRES("pres");

    private final String code;
}
