package ru.nikitavov.soup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.nikitavov.soup.web.security.AppProperties;


@SpringBootApplication()
@EnableConfigurationProperties({AppProperties.class})
public class KkepApiApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(KkepApiApplication.class, args);

    }
}
