package com.sid.gl.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="td_book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private String isbn;
    @Lob
    private String description;
    @Embedded
    private Author author;
    private String filebook;

    @Column(name = "book_type")
    private String bookType;

    @Column(name = "file_name")
    private String fileName;
    private double price;

}
