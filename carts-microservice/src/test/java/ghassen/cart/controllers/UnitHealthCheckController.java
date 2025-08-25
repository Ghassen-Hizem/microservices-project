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
        /**
         * Creates and exposes a HealthCheckController bean for the test Spring context.
         *
         * @return a new HealthCheckController instance
         */
        @Bean
        public HealthCheckController healthCheckController() {
            return new HealthCheckController();
        }

        /**
         * Provides a Mockito-mocked MongoTemplate for the test application context.
         *
         * <p>Used to satisfy HealthCheckController's dependency on a MongoTemplate without
         * connecting to a real MongoDB instance.
         *
         * @return a Mockito mock of {@link org.springframework.data.mongodb.core.MongoTemplate}
         */
        @Bean
        public MongoTemplate mongoTemplate() {
            MongoTemplate mongoTemplate = mock(MongoTemplate.class);
            return mongoTemplate;
        }
    }
}
