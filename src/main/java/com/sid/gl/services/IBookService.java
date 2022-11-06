package com.sid.gl.services;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.exceptions.BooknotFoundException;
import com.sid.gl.exceptions.BusinessValidationException;
import com.sid.gl.models.Book;

import java.util.List;

public interface IBookService {
    Book addBook(BookRequestDto bookRequestDto) throws BusinessValidationException;
    List<BookResponseDTO> listBooks() throws BusinessValidationException;
    BookResponseDTO getBook(Long id) throws BooknotFoundException;
}
