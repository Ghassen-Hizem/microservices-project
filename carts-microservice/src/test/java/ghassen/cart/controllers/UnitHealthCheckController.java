package ghassen.cart.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import ghassen.cart.entities.HealthCheck;

import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UnitHealthCheckController {

    @Autowired
    private HealthCheckController healthCheckController;

    @Test
    public void shouldGetHealth() {
       Map<String, List<HealthCheck>> results = this.healthCheckController.getHealth();
       assertThat(results.get("health").size(), is(equalTo(2)));
    }

    @Configuration
    static class HealthCheckControllerTestConfiguration {
        @Bean
        public HealthCheckController healthCheckController() {
            return new HealthCheckController();
        }

        @Bean
        public MongoTemplate mongoTemplate() {
            MongoTemplate mongoTemplate = mock(MongoTemplate.class);
            return mongoTemplate;
        }
    }
}
