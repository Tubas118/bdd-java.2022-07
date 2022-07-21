package com.example.bdd.java.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.bdd.java.entities.PersonEntity;
import com.example.bdd.java.models.Person;
import com.example.bdd.java.models.PersonsCriteria;
import com.example.bdd.java.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
		assumeThat(optPerson).isPresent();
		
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
	@Test
	public void testGetPersonListByLastNameCriteria() throws Exception {
		// GIVEN
		final String findLastName = "Doe";
		final Example<PersonEntity> criteria = Example.of(PersonEntity.builder().lastname(findLastName).build());
		final List<PersonEntity> expectedPersons = personRepository.findAll(criteria);
		assumeThat(expectedPersons).isNotEmpty();
		assumeThat(expectedPersons.size()).isGreaterThanOrEqualTo(2);
		
		// AND
		final PersonsCriteria personCriteria = PersonsCriteria.builder().lastname(findLastName).build();
		
		// WHEN
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/tdd-examples/persons/searches")
				.content(objectMapper.writeValueAsBytes(personCriteria))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		// THEN
		List<PersonEntity> responsePersons = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PersonEntity[].class));
		assertThat(responsePersons).isEqualTo(expectedPersons);
	}
}
