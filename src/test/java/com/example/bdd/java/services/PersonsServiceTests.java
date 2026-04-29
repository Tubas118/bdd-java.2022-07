package com.example.bdd.java.services;

import com.example.bdd.java.entities.PersonEntity;
import com.example.bdd.java.models.Person;
import com.example.bdd.java.models.PersonsCriteria;
import com.example.bdd.java.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class PersonsServiceTests {

	private static final ExampleMatcher findPersonsByCriteriaExampleForTest = ExampleMatcher.matchingAll()
			.withMatcher("firstname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher("lastname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher("id", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

	@MockBean
	private PersonRepository personRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PersonsService personsService;

	PersonEntity[] personEntities;
	Person[] persons;

	@BeforeEach
	void init() {
		final List<String> source = Arrays.asList(
				"entity1,Robert,Robinson",
				"entity2,Robin,Robinson",
				"entity3,Judy,Robinson",
				"entity4,Jake,Robinson",
				"entity5,Simon,Robinson",
				"entity6,Zachary,Smith"
		);
		List<PersonEntity> personEntityList = new ArrayList<>();
		List<Person> personList = new ArrayList<>();
		for (String line : source) {
			final String[] split = line.split(",");
			personEntityList.add(PersonEntity.builder()
					.id(split[0])
					.firstname(split[1])
					.lastname(split[2])
					.build());
			personList.add(Person.builder()
					.id(split[0])
					.firstname(split[1])
					.lastname(split[2])
					.build());
		}

		personEntities = personEntityList.toArray(new PersonEntity[0]);
		persons = personList.toArray(new Person[0]);
	}

	@Test
	@DisplayName("should successfully find person by id")
	void testFindPersonById() {
		// given
		final String key = "kirk-1";
		final PersonEntity personEntity = PersonEntity.builder()
				.id(key)
				.firstname("James")
				.lastname("Kirk")
				.build();
		doReturn(Optional.of(personEntity))
				.when(personRepository).findById(key);

		// when
		Person foundPerson = personsService.findPerson(key);

		// then
		assertThat(foundPerson).isNotNull();
		assertThat(foundPerson.getId()).isEqualTo(personEntity.getId());
		assertThat(foundPerson.getFirstname()).isEqualTo(personEntity.getFirstname());
		assertThat(foundPerson.getLastname()).isEqualTo(personEntity.getLastname());
	}

	@Test
	@DisplayName("should return null when person not found by id")
	void testPersonNotFoundById() {
		// given
		final String key = "kirk-1";
		doReturn(Optional.empty())
				.when(personRepository).findById(key);

		// when
		Person foundPerson = personsService.findPerson(key);

		// then
		assertThat(foundPerson).isNull();
	}

	@Test
	@DisplayName("should return matching person(s) based on submitted criteria")
	void testMatchingPersonsFromCriteria() {
		// given
		final PersonsCriteria personsCriteria = PersonsCriteria.builder()
				.lastname("Doe")
				.build();

		// -- and
		final PersonEntity expectedEntityCriteria = PersonEntity.builder()
				.lastname("Doe")
				.build();

		// -- and
		final List<PersonEntity> mockedPersons = Arrays.asList(
				PersonEntity.builder().id("test-jdoe-1").firstname("Janet").lastname("Doe").build(),
				PersonEntity.builder().id("test.jdoe-2").firstname("Jake").lastname("Doe").build()
		);

		doReturn(mockedPersons)
				.when(personRepository).findAll(Example.of(expectedEntityCriteria, findPersonsByCriteriaExampleForTest));

		// -- and
		final List<Person> expectedPersons = Arrays.asList(
				Person.builder().id("test-jdoe-1").firstname("Janet").lastname("Doe").build(),
				Person.builder().id("test.jdoe-2").firstname("Jake").lastname("Doe").build()
		);

		// when
		List<Person> foundPersons = personsService.findPersonsByCriteria(personsCriteria);

		// then
		assertThat(foundPersons).isNotEmpty();
		assertThat(foundPersons).containsAll(expectedPersons);
	}

	@Test
	@DisplayName("should return empty list when no person records match the criteria")
	void testNoMatchingPersonsFromCriteria() {
		// given
		final PersonsCriteria personsCriteria = PersonsCriteria.builder()
				.lastname("Doe")
				.build();

		// -- and
		final PersonEntity expectedEntityCriteria = PersonEntity.builder()
				.lastname("Doe")
				.build();

		// -- and
		doReturn(Collections.emptyList())
				.when(personRepository).findAll(Example.of(expectedEntityCriteria, findPersonsByCriteriaExampleForTest));

		// when
		List<Person> foundPersons = personsService.findPersonsByCriteria(personsCriteria);

		// then
		assertThat(foundPersons).isEmpty();
	}

	@Test
	@DisplayName("should return person records from fuzzy search without duplicates")
	void testFuzzySearchWithoutDuplicates() {
		// given
		final String wordSegment = "rob";
		final List<PersonEntity> firstnameMatch = Arrays.asList(
				personEntities[0], personEntities[1]
        );
		doReturn(firstnameMatch)
				.when(personRepository).findByFirstnameContainingIgnoreCase(wordSegment);

		// -- and
		final List<PersonEntity> lastnameMatch = Arrays.asList(
				personEntities[0], personEntities[1], personEntities[2],
				personEntities[3], personEntities[4]
		);
		doReturn(lastnameMatch)
				.when(personRepository).findByLastnameContainingIgnoreCase(wordSegment);

		// -- and
		doReturn(Collections.emptyList())
				.when(personRepository).findByIdContainingIgnoreCase(wordSegment);

		// -- and
		final List<Person> expectedPersons = Arrays.asList(
				persons[0], persons[1], persons[2],
				persons[3], persons[4]
		);

		// when
		List<Person> foundPersons = personsService.findPersonByFuzzySearch(wordSegment);

		// then
		assertThat(foundPersons).isNotEmpty();
		assertThat(foundPersons).containsAll(expectedPersons);
	}
}
