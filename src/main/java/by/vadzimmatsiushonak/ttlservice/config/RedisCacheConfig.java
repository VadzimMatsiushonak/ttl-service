package by.vadzimmatsiushonak.ttlservice.config;

import static by.vadzimmatsiushonak.ttlservice.config.CacheConst.CACHE_TTL_SECONDS;
import static by.vadzimmatsiushonak.ttlservice.config.CacheConst.DEFAULT_CACHE_NAME;

import java.time.Duration;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.core.RedisKeyValueAdapter.ShadowCopy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@Slf4j
@RequiredArgsConstructor
@Profile(RedisCacheConfig.REDIS_CACHE)
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP, shadowCopy = ShadowCopy.ON)
public class RedisCacheConfig {

    public static final String REDIS_CACHE = "redis";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(CACHE_TTL_SECONDS))
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
            .transactionAware()
            .withInitialCacheConfigurations(Collections.singletonMap(DEFAULT_CACHE_NAME,
                configuration))
            .build();
    }


    @Bean
    public Cache defaultCache(CacheManager cacheManager) {
        return cacheManager.getCache(DEFAULT_CACHE_NAME);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    @Qualifier("redisTemplate")
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @EventListener
    public void handleRedisStringKeyExpiredEvent(RedisKeyExpiredEvent<String> event) {
        // Redis not providing us with expired event value
        // Maybe we need to use shadow_copy or something like this
        log.info("key '{}', value '{}', cause '{}", new String(event.getId()), event.getValue(), event);
    }


}
