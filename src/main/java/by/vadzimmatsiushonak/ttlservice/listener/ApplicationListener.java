package by.vadzimmatsiushonak.ttlservice.listener;


import static java.util.concurrent.TimeUnit.SECONDS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationListener {

    private final Cache defaultCache;

    @EventListener(ApplicationReadyEvent.class)
    public void startup() throws InterruptedException {
        SECONDS.sleep(1L);
        log.info("Application started");
        defaultCache.put("key", "value");
        log.info("put 'key':'value' in defaultCache");
        ValueWrapper value = defaultCache.get("key");
        log.info("Get 'key':{}", value);
        SECONDS.sleep(6L);
        value = defaultCache.get("key");
        log.info("Get 'key':{}", value);
    }

}
