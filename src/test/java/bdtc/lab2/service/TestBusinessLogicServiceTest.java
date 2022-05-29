package bdtc.lab2.service;

import bdtc.lab2.dao.WeatherRepository;
import bdtc.lab2.listener.Broker;
import bdtc.lab2.model.WeatherDto;
import bdtc.lab2.model.WeatherEntity;
import bdtc.lab2.utils.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestBusinessLogicServiceTest.TestBusinessLogicServiceTestConfiguration.class})
public class TestBusinessLogicServiceTest {

    @Autowired
    private WeatherService testBusinessLogicService;

    @Autowired
    private WeatherRepository testServiceRepository;

    @Test
    public void testCreateAndGet(){
        //create
        WeatherDto weatherDto = EntityUtils.createWeather();

        WeatherEntity weather = testBusinessLogicService.save(weatherDto);

        Assert.assertEquals(weatherDto.getArea(), weather.getArea());
        Mockito.verify(testServiceRepository, Mockito.times(1)).save(weather);

        //getAll
        List<WeatherEntity> weatherList = testBusinessLogicService.getAll();

        Assert.assertEquals(EntityUtils.VALUE_1, weatherList.get(0).getValue());
        Assert.assertEquals(EntityUtils.VALUE_2, weatherList.get(1).getValue());
        Mockito.verify(testServiceRepository, Mockito.times(1)).getAll();

        List<WeatherEntity> result = testServiceRepository.getResult();

        Assert.assertEquals(EntityUtils.TIME_RESULT, result.get(0).getTime());
        Assert.assertEquals(EntityUtils.AREA_1, result.get(0).getArea());
        Assert.assertEquals(EntityUtils.SENSOR_RESULT, result.get(0).getSensor());
        Assert.assertEquals(EntityUtils.RESULT_VALUE, result.get(0).getValue());

        Mockito.verify(testServiceRepository, Mockito.times(1)).getResult();
    }

    @Configuration
    static class TestBusinessLogicServiceTestConfiguration {

        @Bean
        WeatherRepository testServiceRepository() {
            WeatherRepository testServiceRepository = mock(WeatherRepository.class);
            when(testServiceRepository.getById(any())).thenReturn(EntityUtils.createWeather());
            when(testServiceRepository.getAll())
                    .thenReturn(Arrays.asList(EntityUtils.createWeather()
                            , EntityUtils.createWeather_2()));
            when(testServiceRepository.getResult()).thenReturn(EntityUtils.getResult());
            return testServiceRepository;
        }

        @Bean
        Broker testBroker() {
            return mock(Broker.class);
        }

        @Bean
        WeatherService testBusinessLogicService(WeatherRepository testServiceRepository){
            return new WeatherService(testBroker(), testServiceRepository);
        }
    }

}
