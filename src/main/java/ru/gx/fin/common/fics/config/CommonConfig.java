package ru.gx.fin.common.fics.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.gx.fin.common.fics.converters.*;
import ru.gx.fin.common.fics.datacontroller.DataController;

@EnableJpaRepositories("ru.gx.fin.common.fics.repository")
@EntityScan({"ru.gx.fin.common.fics.entities"})
// @EnableConfigurationProperties({ConfigurationPropertiesServiceRedis.class})
public abstract class CommonConfig {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Common">

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="DataController">

    @Bean
    public DataController dataController() {
        return new DataController();
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Converters">
    @Bean
    public CurrencyDtoFromEntityConverter currencyDtoFromEntityConverter() {
        return new CurrencyDtoFromEntityConverter();
    }

    @Bean
    public CurrencyEntityFromDtoConverter currencyEntityFromDtoConverter() {
        return new CurrencyEntityFromDtoConverter();
    }

    @Bean
    public SecurityDtoFromEntityConverter securityDtoFromEntityConverter() {
        return new SecurityDtoFromEntityConverter();
    }

    @Bean
    public SecurityEntityFromDtoConverter securityEntityFromDtoConverter() {
        return new SecurityEntityFromDtoConverter();
    }

    @Bean
    public DerivativeDtoFromEntityConverter derivativeDtoFromEntityConverter() {
        return new DerivativeDtoFromEntityConverter();
    }

    @Bean
    public DerivativeEntityFromDtoConverter derivativeEntityFromDtoConverter() {
        return new DerivativeEntityFromDtoConverter();
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Redis">

    @Bean
    FicsEntitiesUploadingConfiguration ficsEntitiesUploadingConfiguration() {
        return new FicsEntitiesUploadingConfiguration();
    }

    @Bean
    RedisOutcomeCollectionsConfiguration redisOutcomeCollectionsConfiguration() {
        return new RedisOutcomeCollectionsConfiguration("redis-outcome-config");
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
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
