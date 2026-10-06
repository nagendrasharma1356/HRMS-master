package com.papayaCoders.helper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelperClass
{
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
