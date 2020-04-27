package com.example.springprometheus.services;

import com.example.springprometheus.models.Person;
import com.example.springprometheus.repositories.PersonRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepo;
    private final MeterRegistry registry;

    @Override
    public Optional<Person> getPersonById(Long personId) {
        registry.counter("SERVICE_PERSON_GET_BY_ID").increment();

        delayService();

        return  personRepo.findById(personId);
    }

    @Override
    public List<Person> getAllPersons() {
        registry.counter("SERVICE_PERSON_GET_ALL").increment();

        Timer.Sample sample = Timer.start(registry);
        delayService();
        List<Person> list = personRepo.findAll().parallelStream()
                .collect(Collectors.toList());
        sample.stop(registry.timer("SERVICE_PERSON_GET_ALL_TIMER"));

        return list;
    }

    @Override
    public boolean removePerson(Long personId) {
        registry.counter("SERVICE_PERSON_REMOVE").increment();

        delayService();

        personRepo.deleteById(personId);
        return true;
    }

    @Override
    public Optional<Person> saveUpdatePerson(Person person) {
        registry.counter("SERVICE_PERSON_SAVE_UPDATE").increment();

        delayService();

        if(person.getPersonId() == null || personRepo.existsById(person.getPersonId())) {
            Person entity = personRepo.save(person);
            return Optional.of(entity);
        } else {
            return Optional.empty();
        }
    }

    private void delayService() {
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
