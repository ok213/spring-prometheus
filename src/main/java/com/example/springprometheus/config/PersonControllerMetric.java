package com.example.springprometheus.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PersonControllerMetric {

    // how many users are registered
    private final Counter reqAddPerson;
    // how many users left
    private final Counter reqDeletePerson;

    public PersonControllerMetric(MeterRegistry registry) {
        this.reqAddPerson = Counter.builder("REQUESTS_TOTAL_ADD_PERSON")
                .description("=====> ADD PERSON <=====")
                .register(registry);
        this.reqDeletePerson = Counter.builder("REQUESTS_TOTAL_DELETE_PERSON")
                .description("=====> DELETE PERSON <=====")
                .register(registry);
    }

    public void addPersonInc() {
        reqAddPerson.increment();
    }

    public void deletePersonInc() {
        reqDeletePerson.increment();
    }
}
