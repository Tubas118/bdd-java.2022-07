package com.example.bdd.java.services;

import java.util.Optional;

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
		if (foundPerson.isEmpty()) {
			return null;
		}
		return objectMapper.convertValue(foundPerson.get(), Person.class);
	}
	
}
