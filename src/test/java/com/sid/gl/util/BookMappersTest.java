package com.sid.gl.util;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.models.Author;
import com.sid.gl.models.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookMappersTest {

    @Test
    void convertToBook() {
        BookRequestDto bookRequestDto=new BookRequestDto();
        bookRequestDto.setBookType("booktype");
        Author author = new Author();
        author.setName("author");
        author.setResume("resume");
        author.setContacts("contacts");
        bookRequestDto.setAuthor(author);
        bookRequestDto.setDescription("description");
        bookRequestDto.setIsbn("isbn");
        bookRequestDto.setTitle("title");
        bookRequestDto.setId(1L);

        Book result = BookMappers.convertToBook(bookRequestDto);
        assertNotNull(result);
        assertEquals(bookRequestDto.getTitle(),result.getTitle());
    }

    @Test
    void convertToBookResponse() {
    }
}