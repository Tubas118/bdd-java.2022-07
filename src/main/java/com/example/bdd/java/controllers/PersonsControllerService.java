package com.example.bdd.java.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bdd.java.services.PersonsService;
import com.example.bdd.java.api.PersonsApiDelegate;
import com.example.bdd.java.models.Person;
import com.example.bdd.java.models.PersonsCriteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonsControllerService implements PersonsApiDelegate {
	
	private final PersonsService personsService;

	@Override
	public ResponseEntity<Person> getPerson(String personId) {
		Person person = personsService.findPerson(personId);
		if (person == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(person);
	}

	@Override
	public ResponseEntity<List<Person>> searchPersons(PersonsCriteria personsCriteria) {
		List<Person> persons = personsService.findPersonsByCriteria(personsCriteria);
		if (persons == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(persons);
	}

}
