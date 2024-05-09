# TTL-Service

## Spring Cache

### Configuration

To enable the Spring cache mechanism, follow these steps:

1. Add the `@EnableCaching` annotation to your configuration class.

```java

@EnableCaching
public class ApplicationConfig {
    // Configuration code...
}
```

2. Configure the CacheManager bean.

```java

@Bean
public CacheManager cacheManager() {
    CustomCacheManagerImpl cacheManager = new CustomCacheManagerImpl();
    return cacheManager;
}
```

3. Initialize a specific cache with its name.

```java

@Bean
public Cache defaultCache(CacheManager cacheManager) {
    return cacheManager.getCache("DEFAULT_CACHE_NAME");
}
```

### Usage

To use the cache, follow this example:

```java
private final Cache defaultCache;

@EventListener(ApplicationReadyEvent.class)
public void startup() throws InterruptedException {
    log.info("Application started");
    defaultCache.put("key", "value");
    log.info("Put 'key':'value' in defaultCache");
    ValueWrapper value = defaultCache.get("key");
    log.info("Get 'key': {}", value);
    SECONDS.sleep(5L);
    value = defaultCache.get("key");
    log.info("Get 'key': {}", value);
}
```

## Caffeine

To configure Caffeine cache, follow these steps:

1. Create the Caffeine config bean using `Caffeine.newBuilder()`.

```java
Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
```

2. Apply Time-To-Live (TTL) to the cache using `.expireAfterWrite(CACHE_TTL_SECONDS, TimeUnit.SECONDS)`.

```java
caffeine.expireAfterWrite(CACHE_TTL_SECONDS, TimeUnit.SECONDS);
```

<hr>

> NOTE: By default, entries are not evicted every second. They are evicted when the cache is reloaded or data is
> retrieved. To enable evictions every second, use `.scheduler(Scheduler.systemScheduler())`.

3. Configure the eviction listener.

```java
caffeine.evictionListener(evictionListener);
```

4. Create a component for the eviction listener.

```java

@Component
@Slf4j
public class CaffeineEvictionListener implements RemovalListener<Object, Object> {

    @Override
    public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause cause) {
        log.info("Key: '{}', Value: '{}', Cause: '{}'", key, value, cause);
    }
}
```

## Redis

To configure Redis cache, follow these steps:

1. Create the RedisConnectionFactory.

```java

@Bean
public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
}
```

2. Configure the CacheManager.

```java

@Bean
public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(configuration)
        .transactionAware()
        .withInitialCacheConfigurations(Collections.singletonMap(DEFAULT_CACHE_NAME, configuration))
        .build();
}
```

3. Enable TTL for Redis cache.

```java
RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
    .entryTtl(Duration.ofSeconds(CACHE_TTL_SECONDS));
```

4. Enable Redis keyspace events.

```java

@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP, shadowCopy = ShadowCopy.ON)
public class RedisConfig {
    // Configuration code...
}
```

5. Add an event listener to handle expired events.

```java

@EventListener
public void handleRedisStringKeyExpiredEvent(RedisKeyExpiredEvent<String> event) {
    log.info("Key: '{}', Value: '{}', Cause: '{}'", new String(event.getId()), event.getValue(), event);
}
```

<hr>

> NOTE: Redis does not provide us with the expired event value. You may need to use shadow_copy or a similar approach.

### Custom Usage

If you want to use Redis-specific cache, follow these steps:

1. Create a RedisTemplate bean.

```java

@Bean
@Qualifier("redisTemplate")
public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    return template;
}
```

2. Use RedisTemplate or its interface RedisOperations to configure the types of keys and values.

```java

@Autowired
@Qualifier("redisTemplate")
private final RedisOperations<String, String> redisTemplate;

public void updateCache() {
    redisTemplate.opsForList().leftPush("key", "value");
    redisTemplate.opsForList().leftPop("key");
}
```

<hr>

> NOTE: The TTL eviction handler will not work in this case because it is onlyconfigured for the CacheManager.
> Therefore, TTL will not work properly.

## Docker

### Redis

Standalone Redis instance:

```
docker-compose -f docker/redis-compose.yml up -d
```

### Kafka

Standalone Kafka instance with Zookeeper:

```
docker-compose -f docker/redis-compose.yml up -d
```
