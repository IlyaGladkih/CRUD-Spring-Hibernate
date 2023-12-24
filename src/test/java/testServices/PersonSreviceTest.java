package testServices;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.entity.Person;
import ru.test.exceptions.NoSuchPersonException;
import ru.test.repository.PersonRepository;
import ru.test.service.PersonService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PersonSreviceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void handleGetByIdIsExists_validReturnPerson(){
        int id = 1;
        Person person = Person.builder().person_id(1).fio("Ivan Ivanov").build();
        Mockito.when(personRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(person));

        Person byId = personService.getById(id);

        Assertions.assertEquals(byId,person);
        Mockito.verify(personRepository,Mockito.only()).findById(id);
    }

    @Test
    void handleGetByIdIsNotExists_validReturnException(){
        int id = 1;

        Mockito.when(personRepository.findById(Mockito.anyInt())).thenThrow(NoSuchPersonException.class);

        Assertions.assertThrows(NoSuchPersonException.class,()->{
            personService.getById(id);
        });
    }

    @Test
    void handleSavePerson_ValidResultIsOnceInvoke(){
        Person person = Person.builder().person_id(1).fio("Ivan Ivanov").build();

        personService.save(person);

        Mockito.verify(personRepository,Mockito.only()).save(person);
    }

    @Test
    void handleUpdatePerson_ValidResultIsOnceInvoke(){
        int id = 1;
        Person person = Person.builder().person_id(1).fio("Ivan Ivanov").build();
        Mockito.when(personRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(person));

        personService.update(person,id);

        Mockito.verify(personRepository,Mockito.only()).findById(id);
    }

    @Test
    void handleDeletePerson_ValidResultOnceInvoke(){
        int id = 1;

        personService.delete(id);

        Mockito.verify(personRepository,Mockito.only()).deleteById(id);
    }
}
