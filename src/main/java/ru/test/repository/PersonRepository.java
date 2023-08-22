package ru.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.test.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person,Integer> {
}
