package com.example.bdd.java.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.bdd.java.entities.PersonEntity;
import com.example.bdd.java.entities.PersonEntity.PersonEntityBuilder;
import com.example.bdd.java.models.Person;
import com.example.bdd.java.models.PersonsCriteria;
import com.example.bdd.java.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonsService {
	
	private static final ExampleMatcher findPersonsByCriteriaExample = ExampleMatcher.matchingAll()
			.withMatcher("firstname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher("lastname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

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
		if (personsCriteria == null) {
			return null;
		}
		final PersonEntityBuilder entityCriteria = PersonEntity.builder();
		
		if (StringUtils.hasText(personsCriteria.getFirstname())) {		
			entityCriteria.firstname(personsCriteria.getFirstname());
		}
		
		if (StringUtils.hasText(personsCriteria.getLastname())) {		
			entityCriteria.lastname(personsCriteria.getLastname());
		}
		
		// TODO - this doesn't fit nicely into the JPA Example. Find another solution.
		//if (StringUtils.hasText(personsCriteria.getId())) {		
		//	entityCriteria.id(personsCriteria.getId());
		//}
		
		List<PersonEntity> foundPersons = personRepository.findAll(Example.of(entityCriteria.build(), findPersonsByCriteriaExample));
		return (CollectionUtils.isEmpty(foundPersons))
				? null
				: Arrays.asList(objectMapper.convertValue(foundPersons, Person[].class)); 
	}
	
}
