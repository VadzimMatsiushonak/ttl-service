package by.vadzimmatsiushonak.ttlservice.config;

import by.vadzimmatsiushonak.ttlservice.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public NewTopic loggingTopic() {
        return TopicBuilder.name(kafkaProperties.getTopic())
            .partitions(4)
            .replicas(1)
            .build();
    }

}
