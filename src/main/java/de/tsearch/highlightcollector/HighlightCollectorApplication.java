package de.tsearch.highlightcollector;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HighlightCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighlightCollectorApplication.class, args);
    }

    @Bean
    CommandLineRunner afterStart() {
        return args -> {
        };
    }

}
