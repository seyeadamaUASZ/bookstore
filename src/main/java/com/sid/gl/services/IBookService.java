package com.sid.gl.services;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.exceptions.BooknotFoundException;
import com.sid.gl.exceptions.BusinessValidationException;
import com.sid.gl.models.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IBookService {
    Book addBook(BookRequestDto bookRequestDto) throws BusinessValidationException;
    List<BookResponseDTO> listBooks() throws BusinessValidationException;
    BookResponseDTO getBook(Long id) throws BooknotFoundException;

    Book findByFileName(String fileName);

    BookResponseDTO createBookWithFile(String request, Optional<MultipartFile> file) throws IOException;

    byte[] getObject(String key);
}
