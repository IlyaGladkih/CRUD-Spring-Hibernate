package ru.test.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "personId")
@EqualsAndHashCode
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "author")
    private String author;

    @Column(name = "year_of_publication")
    private int yearOfPublication;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id",referencedColumnName = "person_id")
    private Person personId;

    @Column(name = "Date_of_taken")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfTaken;
}
