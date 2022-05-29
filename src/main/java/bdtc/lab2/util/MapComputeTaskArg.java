package bdtc.lab2.util;

import bdtc.lab2.model.WeatherEntity;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Getter
@Setter
public class MapComputeTaskArg {
    private List<WeatherEntity> arg;
    private Method method;
    private Object object;

    public void invoke(List<WeatherEntity> weatherEntities) throws InvocationTargetException, IllegalAccessException {
        Object[] parameters = new Object[1];
        parameters[0] = weatherEntities;
        method.invoke(object,parameters);
    }
    public MapComputeTaskArg(List<WeatherEntity> arg, Method method, Object object){
        this.arg = arg;
        this.method = method;
        this.object = object;
    }

    public MapComputeTaskArg(Method method, Object object){
        this.method = method;
        this.object = object;
    }
}
