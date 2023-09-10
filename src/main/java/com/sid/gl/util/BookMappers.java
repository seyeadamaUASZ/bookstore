package com.sid.gl.util;


import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Book;
import org.springframework.beans.BeanUtils;

public class BookMappers {

    public static Book convertToBook(BookRequestDto bookRequestDto){
        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setDescription(bookRequestDto.getDescription());
        book.setBookType(bookRequestDto.getBookType());
        return book;
    }

    public static BookResponseDTO convertToBookResponse(Book book){
        BookResponseDTO response = new BookResponseDTO();
        response.setId(book.getId());
        response.setIsbn(book.getIsbn());
        response.setBookType(book.getBookType());
        response.setDescription(book.getDescription());
        response.setAuthor(book.getAuthor());
        return response;
    }

}
