package ru.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.test.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    public List<Book> findBooksByBookNameStartingWith(String name);
}
