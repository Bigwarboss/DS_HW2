package bdtc.lab2.config;

import bdtc.lab2.controller.WeatherController;
import bdtc.lab2.dao.WeatherRepository;
import bdtc.lab2.listener.Broker;
import bdtc.lab2.model.WeatherDto;
import bdtc.lab2.model.WeatherEntity;
import bdtc.lab2.service.WeatherService;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

@Configuration
@Import({IgniteConf.class, KafkaConsumerConfig.class, KafkaProducerConfig.class})
public class ServiceConf {


    @Bean
    WeatherRepository weatherRepository(Ignite ignite, CacheConfiguration<UUID, WeatherEntity> igniteWeatherCacheConfiguration,
                                        CacheConfiguration<UUID, WeatherEntity> igniteWeatherResultCacheConfiguration) {
        return new WeatherRepository(ignite, igniteWeatherCacheConfiguration, igniteWeatherResultCacheConfiguration);
    }

    @Bean
    Broker broker(KafkaTemplate<UUID, WeatherDto> kafkaTemplate) {
        return new Broker(kafkaTemplate);
    }

    @Bean
    WeatherService weatherService(Broker broker, WeatherRepository weatherRepository) {
        return new WeatherService(broker, weatherRepository);
    }

    @Bean
    WeatherController weatherController(WeatherService weatherService) {
        return new WeatherController(weatherService);
    }
}
