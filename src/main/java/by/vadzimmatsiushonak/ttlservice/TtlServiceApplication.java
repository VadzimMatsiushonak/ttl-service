package by.vadzimmatsiushonak.ttlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TtlServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtlServiceApplication.class, args);
    }

}
