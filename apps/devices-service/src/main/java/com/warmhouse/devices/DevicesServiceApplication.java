package com.warmhouse.devices;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevicesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevicesServiceApplication.class, args);
    }

    @Bean
    public NewTopic deviceEventsTopic() {
        return TopicBuilder.name(DeviceEventPublisher.EVENTS_TOPIC).build();
    }

    @Bean
    public NewTopic deviceReadingsTopic() {
        return TopicBuilder.name(DeviceEventPublisher.READINGS_TOPIC).build();
    }
}
