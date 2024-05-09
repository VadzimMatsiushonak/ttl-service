package by.vadzimmatsiushonak.ttlservice.listener;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CaffeineEvictionListener implements RemovalListener<Object, Object> {

    @Override
    public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause cause) {
        log.info("key '{}', value '{}', cause '{}'", key, value, cause);
    }

}