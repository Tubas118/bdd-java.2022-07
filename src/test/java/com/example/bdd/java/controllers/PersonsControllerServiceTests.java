package com.example.bdd.java.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.bdd.java.entities.PersonEntity;
import com.example.bdd.java.models.Person;
import com.example.bdd.java.models.PersonsCriteria;
import com.example.bdd.java.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonsControllerServiceTests {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PersonRepository personRepository;
	
	// AC #1
	@DisplayName("AC-1.1: should get person by id")
	@Test
	public void testGetPersonByIdSuccess() throws Exception {
		// GIVEN
		final String findId = "mock-jdoe-1";
		final Optional<PersonEntity> optPerson = personRepository.findById(findId);
		assertThat(optPerson).isPresent();
		
		// AND
		Person expectedPerson = Person.builder()
				.id(findId)
				.firstname("Jane")
				.lastname("Doe")
				.build();
		
		// WHEN
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/tdd-examples/persons/" + findId)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		// THEN
		assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedPerson));
	}
	
	@DisplayName("AC-1.2: should get list of person records by lastname criteria")
	@ParameterizedTest
	@MethodSource("personCriteriaArguments")
	public void testGetPersonListByLastNameCriteria(Example<PersonEntity> criteria, PersonsCriteria personsCriteria) throws Exception {
		// GIVEN
		final List<PersonEntity> expectedPersons = personRepository.findAll(criteria);
		assertThat(expectedPersons).isNotEmpty();
		assertThat(expectedPersons.size()).isGreaterThanOrEqualTo(2);
		
		// WHEN
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/tdd-examples/persons/searches")
				.content(objectMapper.writeValueAsBytes(personsCriteria))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		// THEN
		List<PersonEntity> responsePersons = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PersonEntity[].class));
		assertThat(responsePersons).isEqualTo(expectedPersons);
	}
	
	private static final ExampleMatcher criteriaExampleMatcher = ExampleMatcher.matchingAll()
			.withMatcher("firstname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher("lastname", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
	
	private static Stream<Arguments> personCriteriaArguments() {
		
		return Stream.of(
				arguments(
						Example.of(PersonEntity.builder().lastname("Doe").build(), criteriaExampleMatcher),
						PersonsCriteria.builder().lastname("Doe").build()
						),
				arguments(
						Example.of(PersonEntity.builder().lastname("doe").build(), criteriaExampleMatcher),
						PersonsCriteria.builder().lastname("doe").build()
						),
				arguments(
						Example.of(PersonEntity.builder().lastname("do").build(), criteriaExampleMatcher),
						PersonsCriteria.builder().lastname("do").build()
						),
				arguments(
						Example.of(PersonEntity.builder().lastname("e").build(), criteriaExampleMatcher),
						PersonsCriteria.builder().lastname("e").build()
						)
				);
	}
}
