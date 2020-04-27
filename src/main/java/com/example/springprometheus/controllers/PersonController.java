package com.example.springprometheus.controllers;

import com.example.springprometheus.config.PersonControllerMetric;
import com.example.springprometheus.models.Person;
import com.example.springprometheus.services.PersonService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private final PersonControllerMetric metric;

    /**
     *
     * @return expose GET endpoint to return {@link List} of all available persons
     */
    @GetMapping
    public List<Person> getAllPerson() {
        return personService.getAllPersons();
    }

    /**
     *
     * @param personId supplied as path variable
     * @return expose GET endpoint to return  {@link Person} for the supplied person id
     * return HTTP 404 in case person is not found in database
     */
    @GetMapping(value = "/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable("personId") Long personId) {
        return personService.getPersonById(personId)
                .map(person -> {
                    return ResponseEntity.ok(person);
                })
                .orElseGet(() -> {
                    return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
        });
    }

    /**
     *
     * @param person JSON body
     * @return  expose POST mapping and return newly created person in case of successful operation
     * return HTTP 417 in case of failure
     */
    @PostMapping
    public ResponseEntity<Person> addNewPerson(@RequestBody Person person) {
        // Increase the counter metric
        metric.addPersonInc();

        return personService.saveUpdatePerson(person)
                .map(p -> {
                    return ResponseEntity.ok(p);
                })
                .orElseGet(() -> {
                    return new ResponseEntity<Person>(HttpStatus.EXPECTATION_FAILED);
        });
    }

    /**
     *
     * @param person JSON body
     * @return  expose PUT mapping and return newly created or updated person in case of successful operation
     * return HTTP 417 in case of failure
     *
     */
    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        return personService.saveUpdatePerson(person)
                .map(p -> {
                    return ResponseEntity.ok(p);
                })
                .orElseGet(() -> {
                    return new ResponseEntity<Person>(HttpStatus.EXPECTATION_FAILED);
        });
    }

    /**
     *
     * @param personId person id to be deleted
     * @return expose DELETE mapping and return success message if operation was successful.
     *  return HTTP 417 in case of failure
     *
     */
    @DeleteMapping(value = "/{personId}")
    public ResponseEntity<String> deletePerson(@PathVariable("personId") Long personId) {
        // Increase the counter metric
        metric.deletePersonInc();

        if (personService.removePerson(personId)) {
            return ResponseEntity.ok("Person with id : " + personId + " removed");
        } else {
            return new ResponseEntity<String>("Error deleting enitty ", HttpStatus.EXPECTATION_FAILED);
        }
    }

}
