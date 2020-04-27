package com.example.springprometheus.config;

import com.example.springprometheus.models.Person;
import com.example.springprometheus.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseInit implements CommandLineRunner {

    private final PersonRepository personRepo;

    @Override
    public void run(String... args) throws Exception {

        personRepo.save(new Person("Ivan", "Ivanov", "ivan@gmail.com"));
        personRepo.save(new Person("Petr", "Petrov", "petr@mail.ru"));
        personRepo.save(new Person("Sidor", "Sidorov", "sidor@yandex.ru"));

    }
}
