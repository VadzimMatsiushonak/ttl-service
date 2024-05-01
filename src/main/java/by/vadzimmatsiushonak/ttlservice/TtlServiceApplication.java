package by.vadzimmatsiushonak.ttlservice;

import static java.util.concurrent.TimeUnit.SECONDS;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class TtlServiceApplication {

    @Autowired
    private Cache defaultCache;

    public static void main(String[] args) {
        SpringApplication.run(TtlServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startup() throws InterruptedException {
        log.info("Application started");
        defaultCache.put("key", "value");
        log.info("put 'key':'value' in defaultCache");
        ValueWrapper value = defaultCache.get("key");
        log.info("Get 'key':{}", value);
        SECONDS.sleep(5l);
        value = defaultCache.get("key");
        log.info("Get 'key':{}", value);
    }

}
