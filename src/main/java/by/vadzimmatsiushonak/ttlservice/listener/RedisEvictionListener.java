package by.vadzimmatsiushonak.ttlservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisEvictionListener {

    @EventListener
    public void handleRedisStringKeyExpiredEvent(RedisKeyExpiredEvent<String> event) {
        // Redis not providing us with expired event value
        // Maybe we need to use shadow_copy or something like this
        log.info("key '{}', value '{}', cause '{}", new String(event.getId()), event.getValue(), event);
    }

}
