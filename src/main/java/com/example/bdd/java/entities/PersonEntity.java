package com.example.bdd.java.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
public class PersonEntity {

	@Id
	private String id;
	private String firstname;
	private String lastname;
	
}
