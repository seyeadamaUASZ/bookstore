package com.sid.gl.repository;

import com.sid.gl.models.Author;
import com.sid.gl.models.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository repository;

    @Test
     void should_create_book(){
        Book book = new Book();
        Author author = new Author();
        author.setName("author");
        author.setResume("resume");
        author.setContacts("contacts");
        book.setBookType("type");
        book.setId(1L);
        book.setDescription("Description");
        book.setTitle("title");
        book.setAuthor(author);

        Book result = repository.save(book);
        assertNotNull(result);
    }
    @Test
    void should_list_books(){
        List<Book> lists = repository.findAll();
        assertEquals(0,lists.size());
    }

    @Test
    void should_find_Book(){
        Book book = new Book();
        Author author = new Author();
        author.setName("author");
        author.setResume("resume");
        author.setContacts("contacts");
        book.setBookType("type");
        book.setId(1L);
        book.setDescription("Description");
        book.setTitle("title");
        book.setAuthor(author);

        Book result = repository.save(book);

        Optional<Book> expected = repository.findById(result.getId());
        assertTrue(expected.isPresent());
    }

}