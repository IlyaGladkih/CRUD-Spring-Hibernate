package ru.test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private int person_id;

    @Column(name = "fio")
    private String fio;

    @Column(name = "birthday")
    private int birthday;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "personId")
    private List<Book> bookId = new ArrayList<>();

    public void addBook(Book book){
        bookId.add(book);
    }

    public void removeBook(Book book){
        bookId.remove(book);
    }
}
