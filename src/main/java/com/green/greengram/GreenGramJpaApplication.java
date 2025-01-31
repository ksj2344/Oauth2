package com.green.greengram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //auditing 기능 활성화
@ConfigurationPropertiesScan //활성화가 되어야지만 가동된다
@SpringBootApplication
public class GreenGramJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenGramJpaApplication.class, args);
    }

}
