package ru.test.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.entity.Book;
import ru.test.entity.Person;
import ru.test.exceptions.NoSuchBookException;
import ru.test.exceptions.NoSuchPersonException;
import ru.test.repository.BookRepository;
import ru.test.repository.PersonRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private BookRepository bookRepository;

    private PersonRepository personRepository;

    @Autowired
    public BookService(BookRepository bookRepository,  PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> getAllBook(Optional<String> page, Optional<String> bookPerPage, boolean sort){
        int p = 0,bPP = 0;
        try {
            p = Integer.parseInt(page.get());
        }catch (Exception e){
            p = 0;
        }
        try {
            bPP = Integer.parseInt(bookPerPage.get());
        }catch (Exception e){
            bPP = 0;
        }
        if (sort) return bookRepository.findAll(PageRequest.of(p,bPP,Sort.by("yearOfPublication"))).getContent();
        else return bookRepository.findAll(PageRequest.of(p,bPP)).getContent();
    }

    public List<Book> getAllBook(boolean sort){
        if(sort) return bookRepository.findAll(Sort.by("yearOfPublication"));
        else return bookRepository.findAll();
    }

    public Book getById(int id){
        return bookRepository.findById(id).orElseThrow(NoSuchBookException::new);
    }

    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }
    @Transactional
    public void update(Book book,int id){
        Book book1 = bookRepository.findById(id).orElseThrow(NoSuchBookException::new);
        book1.setBookName(book.getBookName());
        book1.setAuthor(book.getAuthor());
        book1.setYearOfPublication(book.getYearOfPublication());
        book1.setPersonId(book.getPersonId());
    }

    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }
    @Transactional
    public void pushBook(Person person,int id){
        Book book = bookRepository.findById(id).orElseThrow(NoSuchBookException::new);
        Person person1 = personRepository.findById(person.getPerson_id()).orElseThrow(NoSuchPersonException::new);
        book.setDateOfTaken(new Date());
        book.setPersonId(person1);
        person1.addBook(book);
    }

    @Transactional
    public void release(int id){
        Book book = bookRepository.findById(id).orElseThrow(NoSuchBookException::new);
        book.setPersonId(null);
        book.setDateOfTaken(null);
    }

    public List<Book> getBooksLike(String name){
        return bookRepository.findBooksByBookNameStartingWith(name);
    }

    public boolean chekDate(Book book){
        LocalDate now = LocalDate.now();
        LocalDate now1 = now.minusDays(10);
        Optional<Date> dateOfTaken = Optional.ofNullable(book.getDateOfTaken());
        if(dateOfTaken.isPresent()){
            Instant instant = dateOfTaken.get().toInstant();
            LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.systemDefault());
            if(localDate.isBefore(now1)) return true;
            else return false;
        }else return false;
    }
}
