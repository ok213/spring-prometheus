package com.example.springprometheus.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Lazy
@Component
public class DataSourceStatusProbeConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MeterRegistry meterRegistry;

    @Bean
    public
    DataSourceStatusProbe dataSourceStatusProbe(DataSource dataSource) {
        return new DataSourceStatusProbe(dataSource);
    }
}
