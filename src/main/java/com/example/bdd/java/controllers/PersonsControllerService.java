package com.example.bdd.java.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bdd.java.services.PersonsService;
import com.example.bdd.java.api.PersonsApiDelegate;
import com.example.bdd.java.models.Person;

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

}
