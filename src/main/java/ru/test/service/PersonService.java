package ru.test.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.entity.Person;
import ru.test.exceptions.NoSuchPersonException;
import ru.test.repository.PersonRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons(){
        return personRepository.findAll();
    }

    public Person getById(int id){
        Person person = personRepository.findById(id).orElseThrow(NoSuchPersonException::new);
        Hibernate.initialize(person.getBookId());
        return person;
    }

    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }

    public void update(Person person,int id){
        Person updatedPerson = personRepository.findById(id).orElseThrow(NoSuchPersonException::new);
        updatedPerson.setFio(person.getFio());
        updatedPerson.setBirthday(person.getBirthday());
    }

    public void delete(int id){
        personRepository.deleteById(id);
    }
}
