package bdtc.lab2.listener;

import bdtc.lab2.model.WeatherDto;
import bdtc.lab2.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
@AllArgsConstructor
public class Listener {

    private final WeatherService weatherService;

    @KafkaListener(topics = "weather")
    public void messageListener(WeatherDto dto) {
        System.out.println("<<<<<<<<<<<<<< Listener catch: \n" + dto);
        weatherService.save(dto);
    }
}
