package bdtc.lab2.controller;

import bdtc.lab2.model.WeatherDto;
import bdtc.lab2.model.WeatherEntity;
import bdtc.lab2.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("weather")
@AllArgsConstructor
@Component
public class WeatherController {

    private WeatherService weatherService;

    @PostMapping(path = {"/save"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRace(@RequestBody WeatherDto weatherDto) {
        weatherService.sendToSave(weatherDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = {"/save/list"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRacesList(@RequestBody List<WeatherDto> list) {
        weatherService.sendListToSave(list);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = {"/get/all"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeatherEntity>> getAll() {
        List<WeatherEntity> weatherEntities = weatherService.getAll();
        return new ResponseEntity<>(weatherEntities, HttpStatus.OK);
    }

    @GetMapping(path = {"/get/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherEntity> getFlight(@PathVariable String id) {
        WeatherEntity flightEntity = weatherService.getById(UUID.fromString(id));
        return new ResponseEntity<>(flightEntity, HttpStatus.OK);
    }

    @GetMapping(path = {"/compute"})
    public ResponseEntity<Void> compute() {
        weatherService.compute();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = {"/result"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeatherEntity>> getResults() {
        List<WeatherEntity> result = weatherService.getResult();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
