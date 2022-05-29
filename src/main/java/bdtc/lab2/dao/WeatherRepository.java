package bdtc.lab2.dao;

import bdtc.lab2.config.IgniteConf;
import bdtc.lab2.model.SensorType;
import bdtc.lab2.model.WeatherEntity;
import bdtc.lab2.util.MapComputeTaskArg;
import bdtc.lab2.util.MapEventCountTask;
import lombok.AllArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@AllArgsConstructor
@Import(IgniteConf.class)
public class WeatherRepository {

    private final Ignite ignite;
    CacheConfiguration<UUID, WeatherEntity> igniteWeatherCacheConfiguration;
    CacheConfiguration<UUID, WeatherEntity> igniteWeatherResultCacheConfiguration;

    public void clearWeatherResult(List<WeatherEntity> aggr) {
        ignite.getOrCreateCache(igniteWeatherResultCacheConfiguration).clear();
        for (WeatherEntity weather : aggr) {
            ignite.getOrCreateCache(igniteWeatherResultCacheConfiguration).put(weather.getId(), weather);
        }
    }

    public void save(WeatherEntity weather) {
        ignite.getOrCreateCache(igniteWeatherCacheConfiguration).put(weather.getId(), weather);
    }

    public void saveList(List<WeatherEntity> list) {
        list.forEach(this::save);
    }

    public WeatherEntity getById(UUID id) {
        return ignite.getOrCreateCache(igniteWeatherCacheConfiguration).get(id);
    }

    public List<WeatherEntity> getAll() {
        Iterable<Cache.Entry<UUID, WeatherEntity>> iterable = () ->
                ignite.getOrCreateCache(igniteWeatherCacheConfiguration).iterator();

        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<WeatherEntity> getResult() {
        Iterable<Cache.Entry<UUID, WeatherEntity>> iterable = () ->
                ignite.getOrCreateCache(igniteWeatherResultCacheConfiguration).iterator();

        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void compute() {
        List<WeatherEntity> entityList = getAll();
        entityList.forEach(this::aggregateByHour);
        entityList.forEach(this::cutSensor);

        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = List.class;

        try {
            MapComputeTaskArg arg = new MapComputeTaskArg(entityList,
                    WeatherRepository.class.getMethod("clearWeatherResult", parameterTypes), this);

            ignite.compute().execute(MapEventCountTask.class, arg);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void aggregateByHour(WeatherEntity dto) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dto.getTime());
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        dto.setTime(new Timestamp(cal.getTimeInMillis()));
    }

    private void cutSensor(WeatherEntity dto) {
        for (SensorType type: SensorType.values()) {
            if (dto.getSensor().contains(type.getCode())){
                dto.setSensor(type.getCode());
            }
        }
    }
}
