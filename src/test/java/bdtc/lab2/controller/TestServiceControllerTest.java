package bdtc.lab2.controller;

import bdtc.lab2.model.WeatherEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import bdtc.lab2.config.ServiceConf;
import bdtc.lab2.config.UtilsConf;
import bdtc.lab2.utils.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceConf.class, UtilsConf.class})
public class TestServiceControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityUtils entityUtils;

    @Before
    public void init() {
        entityUtils.clearWeayhertEntitiesCache();
    }

    @After
    public void clear() {
        entityUtils.clearWeayhertEntitiesCache();
    }

    @Test
    public void get_should_returnWeatherEntityList_when_weatherEntityExists() throws Exception {
        List<WeatherEntity> weatherEntityList = entityUtils.createWeatherList();
        String expectedJson = objectMapper.writeValueAsString(weatherEntityList);

        mvc.perform(get("/weather/get/all").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void get_should_returnWeatherEntity_when_weatherEntityExists() throws Exception {
        WeatherEntity weather = entityUtils.createAndSaveWeatherEntity();
        String expectedJson = objectMapper.writeValueAsString(weather);

        mvc.perform(get("/weather/get/" + weather.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}
