package ru.gx.fin.common.fics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("ru.gx.fin.common.fics.repository")
@EntityScan({"ru.gx.fin.common.fics.entities"})
// @EnableConfigurationProperties({ConfigurationPropertiesServiceRedis.class})
@Configuration
public class CommonConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Test Api")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .email("vladimir.gagarkin@gmail.com")
                                                .url("https://gagarkin.ru")
                                                .name("Vladimir Gagarkin")
                                )
                );
    }
}
