package com.example.bdd.java.controllers;

import com.example.bdd.java.entities.PersonEntity;
import com.example.bdd.java.models.Person;
import com.example.bdd.java.models.PersonsCriteria;
import com.example.bdd.java.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	
	@DisplayName("AC-1.2: should get list of person records by criteria")
	@ParameterizedTest
	@CsvSource({
			"null,null,Doe,2",
			"mock,null,null,3",
			"null,j,Doe,2",
			"mock-cken-1,null,null,1",
			"null,jo,Doe,1",
	})
	public void testGetPersonListByCriteria(String findId, String findFirstname, String findLastname, int expectedRows) throws Exception {
		// GIVEN
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withIgnoreCase();
		PersonEntity personEntityMatch = PersonEntity.builder()
				.id(preprocessValue(findId))
				.firstname(preprocessValue(findFirstname))
				.lastname(preprocessValue(findLastname))
				.build();
		Example<PersonEntity> personEntityCriteria = Example.of(personEntityMatch, matcher);

		// -- sanity check
		final List<PersonEntity> expectedPersons = personRepository.findAll(personEntityCriteria);
		assertThat(expectedPersons).isNotEmpty();
		assertThat(expectedPersons.size()).isGreaterThanOrEqualTo(expectedRows);

		// -- and
		PersonsCriteria personsCriteria = PersonsCriteria.builder()
				.id(preprocessValue(findId))
				.firstname(preprocessValue(findFirstname))
				.lastname(preprocessValue(findLastname))
				.build();

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

	private static String preprocessValue(String value) {
		return StringUtils.isEmpty(value) || "null".equalsIgnoreCase(value)
				? null
				: value;
	}

}
