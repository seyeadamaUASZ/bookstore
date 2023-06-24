package com.sid.gl.services.impl;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Author;
import com.sid.gl.models.Book;
import com.sid.gl.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository repository;
    @InjectMocks
    private BookService service;

    @BeforeEach
    void setUp(){
        //service = new LanguageImpl(languageRepository);
    }


    @Test
    void addBook() {
        Author author = new Author();
        author.setName("author");
        author.setResume("resume");
        author.setContacts("contacts");

        BookRequestDto dto=new BookRequestDto();
        dto.setId(1L);
        dto.setDescription("description");
        dto.setIsbn("isbn");
        dto.setTitle("title");
        dto.setAuthor(author);

        service.addBook(dto);
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(repository,times(1)).save(captor.capture());
        Book captorValue = captor.getValue();

        assertNotNull(captorValue);
        assertEquals("isbn",captorValue.getIsbn());
        assertEquals("title",captorValue.getTitle());
        assertEquals("description",captorValue.getDescription());
    }

    @Test
    void listBooks() {
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

        when(repository.findAll()).thenReturn(Collections.singletonList(book));
        List<BookResponseDTO> result = service.listBooks();
        verify(repository).findAll();
        assertNotNull(result);
        assertEquals(1,result.size());
    }


    @Test
    void getOneBook() {
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

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        BookResponseDTO result = service.getBook(1L);
        assertNotNull(result);
        assertEquals("type",result.getBookType());

    }
}