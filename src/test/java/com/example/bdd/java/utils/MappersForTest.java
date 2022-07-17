package com.example.bdd.java.utils;

import com.example.bdd.java.entities.PersonEntity;
import com.example.bdd.java.models.Person;

public class MappersForTest {

	public static PersonEntity personEntity(Person source) {
		return PersonEntity.builder()
				.id(source.getId())
				.firstname(source.getFirstname())
				.lastname(source.getLastname())
				.build();
	}
	
	public static Person person(PersonEntity source) {
		return Person.builder()
				.id(source.getId())
				.firstname(source.getFirstname())
				.lastname(source.getLastname())
				.build();
	}
	
}
