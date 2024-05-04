package by.vadzimmatsiushonak.ttlservice.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConst {

    public static final String DEFAULT_CACHE_NAME = "default";
    public static final Long CACHE_TTL_SECONDS = 5L;
    public static final Integer CACHE_MAX_SIZE = 100;

}
