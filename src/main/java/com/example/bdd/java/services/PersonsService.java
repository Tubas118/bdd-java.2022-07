package com.example.bdd.java.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.bdd.java.models.PersonsCriteria;
import org.springframework.stereotype.Service;

import com.example.bdd.java.entities.PersonEntity;
import com.example.bdd.java.models.Person;
import com.example.bdd.java.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonsService {
	
	private final PersonRepository personRepository;
	
	private final ObjectMapper objectMapper;

	public Person findPerson(String personId) {
		Optional<PersonEntity> foundPerson = personRepository.findById(personId);
		if (!foundPerson.isPresent()) {	// NOTE: JDK8 Optional does not have "isEmpty()"
			return null;
		}
		return objectMapper.convertValue(foundPerson.get(), Person.class);
	}

	public List<Person> findPersonsByCriteria(PersonsCriteria personsCriteria) {
		return Collections.emptyList();
	}

	public List<Person> findPersonByFuzzySearch(String wordSegment) {
		return Collections.emptyList();
	}

}
