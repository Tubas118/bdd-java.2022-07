package com.example.bdd.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bdd.java.entities.PersonEntity;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonEntity, String> {
    List<PersonEntity> findByFirstnameContainingIgnoreCase(String word);
    List<PersonEntity> findByLastnameContainingIgnoreCase(String word);
    List<PersonEntity> findByIdContainingIgnoreCase(String word);
}
