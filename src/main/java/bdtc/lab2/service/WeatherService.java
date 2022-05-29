package bdtc.lab2.service;

import bdtc.lab2.dao.WeatherRepository;
import bdtc.lab2.listener.Broker;
import bdtc.lab2.model.WeatherDto;
import bdtc.lab2.model.WeatherEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WeatherService {

    private final Broker broker;
    private final WeatherRepository repository;

    public List<WeatherEntity> getAll() {
        return repository.getAll();
    }

    public WeatherEntity getById(UUID id) {
        return repository.getById(id);
    }

    public void sendListToSave(List<WeatherDto> list) {
        list.forEach(this::sendToSave);
    }

    public void sendToSave(WeatherDto dto) {
        broker.send(dto);
    }

    public WeatherEntity save(WeatherDto dto) {
        WeatherEntity entity = new WeatherEntity(dto);
        repository.save(entity);
        return entity;
    }

    public List<WeatherEntity> getResult() {
        return repository.getResult();
    }

    public void compute() {
        repository.compute();
    }
}
