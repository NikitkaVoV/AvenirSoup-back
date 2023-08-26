package ru.nikitavov.soup.general.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.nikitavov.soup.general.jackson.deserializer.IsoDateStringToLocalDateDeserializer;
import ru.nikitavov.soup.general.jackson.serializer.LocalDateToIsoDateStringSerializer;

import java.time.LocalDate;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateToIsoDateStringSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new IsoDateStringToLocalDateDeserializer());


        return Jackson2ObjectMapperBuilder.json()
                .modules(javaTimeModule)
                .build();
    }
}