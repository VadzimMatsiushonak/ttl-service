package by.vadzimmatsiushonak.ttlservice.listener;

import by.vadzimmatsiushonak.ttlservice.model.TTLPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaEventListener {

    private final static String FIRST_PARTITION_PATTERN = "[1L|{}p] : {} : {}";
    private final static String SECOND_PARTITION_PATTERN = "[2L|{}p] : {} : {}";

    @KafkaListener(id = "ttlFirstPartition", topicPartitions = @TopicPartition(topic = "${spring.kafka.topic}",
        partitions = {"0", "1"}), containerFactory = "ttlKafkaListenerContainerFactory")
    public void ttlFirstPartition(@Payload TTLPayload payload,
        @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
        proceedLog(payload, FIRST_PARTITION_PATTERN, partition);
    }

    @KafkaListener(id = "ttlSecondPartition", topicPartitions = @TopicPartition(topic = "${spring.kafka.topic}",
        partitions = {"2", "3"}), containerFactory = "ttlKafkaListenerContainerFactory")
    public void ttlSecondPartition(@Payload TTLPayload payload,
        @Header(KafkaHeaders.RECEIVED_PARTITION) String partition) {
        proceedLog(payload, SECOND_PARTITION_PATTERN, partition);
    }

    private void proceedLog(TTLPayload payload, String pattern, String partition) {
        switch (payload.getType()) {
            case INFO:
                log.info(pattern, partition, payload.getType(), payload.getMessage());
                break;
            case DEBUG:
                log.debug(pattern, partition, payload.getType(), payload.getMessage());
                break;
            case ERROR:
                log.error(pattern, partition, payload.getType(), payload.getMessage());
                break;
            default:
                log.info(pattern, partition, payload.getType(), payload.getMessage());
                break;
        }
    }

}
