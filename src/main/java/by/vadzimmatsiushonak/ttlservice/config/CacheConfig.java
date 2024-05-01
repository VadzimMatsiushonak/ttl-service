package by.vadzimmatsiushonak.ttlservice.config;

import by.vadzimmatsiushonak.ttlservice.listener.CaffeineEvictionListener;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Slf4j
@RequiredArgsConstructor
public class CacheConfig {

    public final static String DEFAULT_CACHE_NAME = "default";
    public final static Long CACHE_TTL_SECONDS = 30L;
    public final static Integer CACHE_MAX_SIZE = 100;

    @Bean
    public Caffeine caffeineConfig(CaffeineEvictionListener evictionListener) {
        return Caffeine
            .newBuilder()
            .expireAfterWrite(CACHE_TTL_SECONDS, TimeUnit.SECONDS)
            .scheduler(Scheduler.systemScheduler()) // Enabled to allow cache 'refreshing' / 'check 'each ~1 second
            .evictionListener(evictionListener)
            .maximumSize(CACHE_MAX_SIZE);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(DEFAULT_CACHE_NAME);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    @Bean
    public Cache defaultCache(CacheManager cacheManager) {
        return cacheManager.getCache(DEFAULT_CACHE_NAME);
    }


}
