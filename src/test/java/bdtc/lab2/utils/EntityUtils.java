package bdtc.lab2.utils;

import bdtc.lab2.dao.WeatherRepository;
import bdtc.lab2.model.WeatherEntity;
import org.apache.ignite.Ignite;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntityUtils {
    private final WeatherRepository testServiceRepository;
    private final Ignite ignite;

    public static final String AREA_1 = "area1";
    public static final Timestamp TIME_1 = Timestamp.valueOf("2022-05-01 09:10:50");
    public static final String SENSOR = "sensor1_temp";
    public static final Double VALUE_1 = 30.0;
    public static final Timestamp TIME_2 = Timestamp.valueOf("2022-05-01 09:05:36");
    public static final Double VALUE_2 = 40.0;

    public static final String SENSOR_RESULT = "temp";
    public static final Double RESULT_VALUE = 35.0;
    public static final Timestamp TIME_RESULT = Timestamp.valueOf("2022-05-01 09:00:00");

    public EntityUtils(WeatherRepository testServiceRepository, Ignite ignite) {
        this.testServiceRepository = testServiceRepository;
        this.ignite = ignite;
    }

    public WeatherEntity createAndSaveWeatherEntity() {
        WeatherEntity weatherEntity = createWeather();
        testServiceRepository.save(weatherEntity);

        return weatherEntity;
    }

    private void save(WeatherEntity weather) {
        testServiceRepository.save(weather);
    }

    public void clearWeayhertEntitiesCache() {
        ignite.getOrCreateCache("weather").clear();
    }

    public static WeatherEntity createWeather() {
        return new WeatherEntity(AREA_1, SENSOR, TIME_1, VALUE_1);
    }

    public static WeatherEntity createWeather_2() {
        return new WeatherEntity(AREA_1, SENSOR, TIME_2, VALUE_2);
    }

    public List<WeatherEntity> createWeatherList() {
        List<WeatherEntity> lsit = Arrays.asList(createWeather(), createWeather_2());
        lsit.forEach(this::save);
        return lsit;
    }

    public static List<WeatherEntity> getResult() {
        return Collections.singletonList(new WeatherEntity(AREA_1, SENSOR_RESULT, TIME_RESULT, RESULT_VALUE));
    }
}
