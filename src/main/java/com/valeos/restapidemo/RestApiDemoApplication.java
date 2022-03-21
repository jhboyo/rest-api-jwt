package com.valeos.restapidemo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class RestApiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiDemoApplication.class, args);
    }


    /**
     *  공통 모듈 Bean 객체 등록
     *
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
