package bdtc.lab2.listener;

import bdtc.lab2.config.KafkaConsumerConfig;
import bdtc.lab2.config.KafkaProducerConfig;
import bdtc.lab2.model.WeatherDto;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@EnableKafka
@AllArgsConstructor
@Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
public class Broker {

    private KafkaTemplate<UUID, WeatherDto> kafkaTemplate;

    public void send(WeatherDto dto) {
        System.out.println(">>>>>>>>>>>>>> Broker send: \n" + dto);
        kafkaTemplate.send("weather", dto);
    }
}
