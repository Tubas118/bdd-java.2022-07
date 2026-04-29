package com.example.bdd.java.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

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
			.withMatcher("lastname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher("id", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

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

		if (StringUtils.isNotBlank(personsCriteria.getFirstname())) {
			entityCriteria.firstname(personsCriteria.getFirstname());
		}

		if (StringUtils.isNotBlank(personsCriteria.getLastname())) {
			entityCriteria.lastname(personsCriteria.getLastname());
		}

		if (StringUtils.isNotBlank(personsCriteria.getId())) {
			entityCriteria.id(personsCriteria.getId());
		}

		List<PersonEntity> foundPersons = personRepository.findAll(Example.of(entityCriteria.build(), findPersonsByCriteriaExample));
		return (CollectionUtils.isEmpty(foundPersons))
				? Collections.emptyList()
				: Arrays.asList(objectMapper.convertValue(foundPersons, Person[].class));
	}

	public List<Person> findPersonByFuzzySearch(String wordSegment) {
		Set<PersonEntity> workingSet = new HashSet<>(personRepository.findByFirstnameContainingIgnoreCase(wordSegment));
		workingSet.addAll(personRepository.findByLastnameContainingIgnoreCase(wordSegment));
		workingSet.addAll(personRepository.findByIdContainingIgnoreCase(wordSegment));
		return Arrays.asList(objectMapper.convertValue(workingSet, Person[].class));
	}

}
