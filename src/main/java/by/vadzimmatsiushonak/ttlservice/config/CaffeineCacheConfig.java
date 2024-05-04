package by.vadzimmatsiushonak.ttlservice.config;

import static by.vadzimmatsiushonak.ttlservice.config.CacheConst.CACHE_MAX_SIZE;
import static by.vadzimmatsiushonak.ttlservice.config.CacheConst.CACHE_TTL_SECONDS;
import static by.vadzimmatsiushonak.ttlservice.config.CacheConst.DEFAULT_CACHE_NAME;

import by.vadzimmatsiushonak.ttlservice.listener.CaffeineEvictionListener;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
@RequiredArgsConstructor
@Profile(CaffeineCacheConfig.CAFFEINE_CACHE)
public class CaffeineCacheConfig {

    public static final String CAFFEINE_CACHE = "caffeine";

    @Bean
    public Caffeine<Object, Object> caffeineConfig(CaffeineEvictionListener evictionListener) {
        return Caffeine
            .newBuilder()
            .expireAfterWrite(CACHE_TTL_SECONDS, TimeUnit.SECONDS)
            .scheduler(Scheduler.systemScheduler()) // Enabled to allow cache 'refreshing' / 'check 'each ~1 second
            .evictionListener(evictionListener)
            .maximumSize(CACHE_MAX_SIZE);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
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
