package com.sid.gl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Book;
import org.springframework.beans.BeanUtils;

public class BookMappers {

    public static Book convertToBook(BookRequestDto bookRequestDto){
        Book book = new Book();
        //BeanUtils.copyProperties(bookRequestDto,book);
        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setFileName(bookRequestDto.getFileName());
        book.setFilebook(bookRequestDto.getFilebook());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setDescription(bookRequestDto.getDescription());
        book.setBookType(bookRequestDto.getBookType());
        return book;
    }

    public static BookResponseDTO convertToBookResponse(Book book){
        BookResponseDTO response = new BookResponseDTO();
        BeanUtils.copyProperties(book,response);
        return response;
    }

    public static String jsonObjectToString(Object o){
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
