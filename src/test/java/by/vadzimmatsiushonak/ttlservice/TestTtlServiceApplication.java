package by.vadzimmatsiushonak.ttlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestTtlServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(TtlServiceApplication::main).with(TestTtlServiceApplication.class).run(args);
    }

}
