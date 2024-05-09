package by.vadzimmatsiushonak.ttlservice.listener;

import static java.util.concurrent.TimeUnit.SECONDS;

import by.vadzimmatsiushonak.ttlservice.model.LoggingType;
import by.vadzimmatsiushonak.ttlservice.model.TTLPayload;
import by.vadzimmatsiushonak.ttlservice.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisEvictionListener {

    private final KafkaTemplate<String, TTLPayload> ttlKafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @EventListener
    public void handleRedisStringKeyExpiredEvent(RedisKeyExpiredEvent<String> event) throws InterruptedException {
        log.info("key '{}', value '{}', cause '{}", new String(event.getId()), event.getValue(), event);
        // Redis not providing us with expired event value
        // Maybe we need to use shadow_copy or something like this
        SECONDS.sleep(1l);
        ttlKafkaTemplate.send(kafkaProperties.getTopic(), new TTLPayload(LoggingType.INFO,
            String.format("key '%s', value '%s', cause '%s'", new String(event.getId()), event.getValue(), event)));
    }

}
