package bdtc.lab2.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class WeatherEntity extends WeatherDto implements Serializable {
    private UUID id;

    public WeatherEntity(String area, String sensor, Timestamp time, double value) {
        super(area, sensor, time, value);
        this.id = UUID.randomUUID();
    }

    public WeatherEntity(WeatherDto weatherDto) {
        super(weatherDto.getArea(), weatherDto.getSensor(), weatherDto.getTime(), weatherDto.getValue());
        this.id = UUID.randomUUID();
    }

}
