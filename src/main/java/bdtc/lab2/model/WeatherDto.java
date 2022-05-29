package bdtc.lab2.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class WeatherDto implements Serializable {
    private String area;
    private String sensor;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;
    private Double value;
}
