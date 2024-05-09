package by.vadzimmatsiushonak.ttlservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.kafka")
@Getter
@Setter
public class KafkaProperties {

    private String bootstrapAddress;
    private String groupId;
    private String topic;

}
