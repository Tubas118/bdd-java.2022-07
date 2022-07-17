package com.example.bdd.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bdd.java.entities.PersonEntity;

public interface PersonRepository extends JpaRepository<PersonEntity, String> {

}
