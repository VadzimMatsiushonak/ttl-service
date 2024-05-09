package by.vadzimmatsiushonak.ttlservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.cache")
@Getter
@Setter
public class CacheProperties {

    private String defaultName;
    private Long ttlSeconds;
    private Integer maxSize;

}
