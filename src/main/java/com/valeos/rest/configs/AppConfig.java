package com.valeos.rest.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     *  공통 모듈 Bean 객체 등록
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
