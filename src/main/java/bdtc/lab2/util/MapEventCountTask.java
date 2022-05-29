package bdtc.lab2.util;

import bdtc.lab2.model.WeatherEntity;
import org.apache.commons.math3.util.Precision;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class MapEventCountTask extends ComputeTaskAdapter<MapComputeTaskArg, Integer> {

    private MapComputeTaskArg updateStatArg;

    @Override
    public @NotNull Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> nodes, @Nullable MapComputeTaskArg arg) throws IgniteException {
        Map<ComputeJob, ClusterNode> map = new HashMap<>();
        Iterator<ClusterNode> it = nodes.iterator();
        this.updateStatArg = new MapComputeTaskArg(arg.getMethod(), arg.getObject());

        Map<String, Map<String, Map<Timestamp, List<WeatherEntity>>>> groupByAll = arg.getArg().stream()
                .collect(Collectors
                        .groupingBy(WeatherEntity::getArea,
                                Collectors.groupingBy(WeatherEntity::getSensor,
                                        Collectors.groupingBy(WeatherEntity::getTime))));

        for (Map<String, Map<Timestamp, List<WeatherEntity>>> groupedBySensorsAndTime: groupByAll.values()) {
            for (Map<Timestamp, List<WeatherEntity>> groupByTime : groupedBySensorsAndTime.values()) {
                for (List<WeatherEntity> weatherList : groupByTime.values()) {

                    if (!it.hasNext()) {
                        it = nodes.iterator();
                    }

                    ClusterNode node = it.next();
                    map.put(new ComputeJobAdapter() {
                        @Override
                        public @NotNull Object execute() {
                            double averageValue = weatherList.stream().mapToDouble(WeatherEntity::getValue)
                                    .average().orElse(0);
                            WeatherEntity weather = weatherList.get(0);

                            return new WeatherEntity(weather.getArea(),weather.getSensor(),
                                    weather.getTime(), Precision.round(averageValue, 3));
                        }
                    }, node);
                }
            }
        }

        return map;
    }

    @Override @Nullable public Integer reduce(List<ComputeJobResult> results) {
        List<WeatherEntity> weatherResult = new ArrayList<>();

        for (ComputeJobResult result : results){
            weatherResult.add(result.<WeatherEntity>getData());
        }

        try {
            updateStatArg.invoke(weatherResult);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return weatherResult.size();
    }
}
