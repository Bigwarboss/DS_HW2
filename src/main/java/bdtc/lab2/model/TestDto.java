package bdtc.lab2.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class TestDto implements Serializable {
    private String name;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //private Timestamp time;
    //private Double values;

    public TestDto(String name) {
        this.name = name;
    }
}
