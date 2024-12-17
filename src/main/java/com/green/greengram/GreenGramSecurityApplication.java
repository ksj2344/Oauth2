package com.green.greengram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan //활성화가 되어야지만 가동된다
@SpringBootApplication
public class GreenGramSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenGramSecurityApplication.class, args);
    }

}
