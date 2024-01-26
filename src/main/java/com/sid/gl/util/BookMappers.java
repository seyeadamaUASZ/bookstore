package com.sid.gl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Book;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BookMappers {

    public static Book convertToBook(BookRequestDto bookRequestDto){
        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setFileName(bookRequestDto.getFileName());
        book.setFilebook(bookRequestDto.getFilebook());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setDescription(bookRequestDto.getDescription());
        book.setBookType(bookRequestDto.getBookType());
        return book;
    }

    public static List<BookResponseDTO> convertListBookResponse(List<Book> lists){
        return CollectionUtils.emptyIfNull(lists)
                .stream().filter(Objects::nonNull)
                .map(BookMappers::convertToBookResponse)
                .collect(Collectors.toList());
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
