package testServices;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.test.entity.Book;
import ru.test.entity.Person;
import ru.test.exceptions.NoSuchBookException;
import ru.test.repository.BookRepository;
import ru.test.repository.PersonRepository;
import ru.test.service.BookService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void handleGetAllBookMethodWithThreeArgumentAndSortedValueFalse_ValidResultReturnList() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(Book.builder()
                    .bookId(1)
                    .bookName("Crime and Punishment")
                    .author("Fyodor Dostoevsky")
                    .dateOfTaken(Date.valueOf("2023-07-07"))
                    .yearOfPublication(1866)
                    .build());
        bookList.add(Book.builder()
                    .bookId(2)
                    .bookName("Moby-Dick")
                    .yearOfPublication(1851)
                    .author("Herman Melville")
                    .dateOfTaken(Date.valueOf("2022-04-05"))
                    .build());
        bookList.add(Book.builder()
                    .bookId(3)
                    .bookName("The Great Gatsby")
                    .yearOfPublication(1925)
                    .author("F. Scott Fitzgerald.")
                    .dateOfTaken(Date.valueOf("2023-07-23"))
                    .build());
        Mockito.when(bookRepository
                .findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(bookList));
        Optional<String> page = Optional.of("1");
        Optional<String> bookPerPage = Optional.of("5");
        boolean sort = false;

        List<Book> allBook = bookService.getAllBook(page, bookPerPage, sort);

        Assertions.assertNotNull(allBook);
        Assertions.assertEquals(allBook,bookList);
    }

    @Test
    void handleTestGetAllBookMethodWithSortedTrue_ValidIsSortedList() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(Book.builder()
                .bookId(1)
                .bookName("Crime and Punishment")
                .author("Fyodor Dostoevsky")
                .dateOfTaken(Date.valueOf("2023-07-07"))
                .yearOfPublication(1866)
                .build());
        bookList.add(Book.builder()
                .bookId(2)
                .bookName("The Great Gatsby")
                .yearOfPublication(1925)
                .author("F. Scott Fitzgerald.")
                .dateOfTaken(Date.valueOf("2023-07-23"))
                .build());
        bookList.add(Book.builder()
                .bookId(3)
                .bookName("Moby-Dick")
                .yearOfPublication(1851)
                .author("Herman Melville")
                .dateOfTaken(Date.valueOf("2022-04-05"))
                .build());
        Mockito.when(bookRepository
                .findAll(Mockito.any(Sort.class))).thenReturn(bookList);
        boolean sort = true;
        bookList.sort(Comparator.comparingInt(Book::getYearOfPublication));

        List<Book> allBook = bookService.getAllBook(sort);

        Assertions.assertNotNull(allBook);
        Assertions.assertEquals(allBook,bookList);
    }

    @Test
    void handleTestGetAllBookMethodWithSortedFalse_ValidIsList() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(Book.builder()
                .bookId(1)
                .bookName("Crime and Punishment")
                .author("Fyodor Dostoevsky")
                .dateOfTaken(Date.valueOf("2023-07-07"))
                .yearOfPublication(1866)
                .build());
        bookList.add(Book.builder()
                .bookId(2)
                .bookName("The Great Gatsby")
                .yearOfPublication(1925)
                .author("F. Scott Fitzgerald.")
                .dateOfTaken(Date.valueOf("2023-07-23"))
                .build());
        bookList.add(Book.builder()
                .bookId(3)
                .bookName("Moby-Dick")
                .yearOfPublication(1851)
                .author("Herman Melville")
                .dateOfTaken(Date.valueOf("2022-04-05"))
                .build());
        Mockito.when(bookRepository
                .findAll()).thenReturn(bookList);
        boolean sort = false;

        List<Book> allBook = bookService.getAllBook(sort);

        Assertions.assertNotNull(allBook);
        Assertions.assertEquals(allBook,bookList);
    }

    @Test
    void handleGetByIdIfBookExists_ValidResultIsBook() {
        Book book = Book.builder()
                .bookId(1)
                .bookName("Crime and Punishment")
                .author("Fyodor Dostoevsky")
                .dateOfTaken(Date.valueOf("2023-07-07"))
                .yearOfPublication(1866)
                .build();
        Mockito.when(bookRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(book));

        Book byId = bookService.getById(1);

        Assertions.assertNotNull(byId);
        Assertions.assertEquals(book,byId);
    }

    @Test
    void handleGetByIdIfBookIsNotExists_ValidResultNull() {

        Mockito.when(bookRepository.findById(Mockito.anyInt())).thenThrow(NoSuchBookException.class);

        Assertions.assertThrows(NoSuchBookException.class,()->{
            Book byId = bookService.getById(1);
        });
    }

    @Test
    void save() {
        Book book = Book.builder()
                .bookId(1)
                .bookName("Crime and Punishment")
                .author("Fyodor Dostoevsky")
                .dateOfTaken(Date.valueOf("2023-07-07"))
                .yearOfPublication(1866)
                .build();
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        bookService.save(book);

        Mockito.verify(bookRepository).save(Mockito.any(Book.class));

    }

    @Test
    void handleUpdateMethod_ValidIsOnceInvoke() {
        int id = 1;
        Book book = Book.builder()
                .bookId(1)
                .bookName("Crime and Punishment")
                .author("Fyodor Dostoevsky")
                .dateOfTaken(Date.valueOf("2023-07-07"))
                .yearOfPublication(1866)
                .build();
        Mockito.when(bookRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(book));

        bookService.update(book,id);

        Mockito.verify(bookRepository,Mockito.only()).findById(id);
    }

    @Test
    void delete() {
        int id = 1;

        bookService.delete(id);

        Mockito.verify(bookRepository,Mockito.only()).deleteById(id);
    }

    @Test
    void pushBook() {
        int id = 1;
        Book book = Book.builder()
                .bookId(1)
                .bookName("Crime and Punishment")
                .author("Fyodor Dostoevsky")
                .dateOfTaken(Date.valueOf("2023-07-07"))
                .yearOfPublication(1866)
                .build();
        Person person = Person.builder().person_id(1).bookId(new ArrayList<Book>()).fio("Ivan Ivanov").build();

        Mockito.when(bookRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(book));
        Mockito.when(personRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(person));

        bookService.pushBook(person,id);

        Mockito.verify(bookRepository,Mockito.only()).findById(id);
        Mockito.verify(personRepository,Mockito.only()).findById(Mockito.anyInt());
    }

    @Test
    void release() {
        int id = 1;
        Book book = Book.builder()
                .bookId(1)
                .bookName("Crime and Punishment")
                .author("Fyodor Dostoevsky")
                .dateOfTaken(Date.valueOf("2023-07-07"))
                .yearOfPublication(1866)
                .build();
        Mockito.when(bookRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(book));

        bookService.release(id);

        Mockito.verify(bookRepository,Mockito.only()).findById(id);
    }

    @Test
    void getBooksLike() {
        String name = "Crime and Punishment";
        Book book = Book.builder()
                .bookId(1)
                .bookName("Crime and Punishment")
                .author("Fyodor Dostoevsky")
                .dateOfTaken(Date.valueOf("2023-07-07"))
                .yearOfPublication(1866)
                .build();
        Mockito.when(bookRepository.findBooksByBookNameStartingWith(Mockito.anyString())).thenReturn(List.of(book));

        List<Book> booksLike = bookService.getBooksLike(name);

        Mockito.verify(bookRepository,Mockito.only()).findBooksByBookNameStartingWith(name);
        Assertions.assertEquals(List.of(book),booksLike);
    }
}