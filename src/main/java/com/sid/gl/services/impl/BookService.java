package com.sid.gl.services.impl;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.exceptions.BooknotFoundException;
import com.sid.gl.exceptions.BusinessValidationException;
import com.sid.gl.models.Book;
import com.sid.gl.repository.BookRepository;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.BookMappers;
import com.sid.gl.util.Translator;
import lombok.AllArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService implements IBookService {
  private BookRepository bookRepository;

  @Override
  public Book addBook(BookRequestDto bookRequestDto) throws BusinessValidationException {
    Book book=new Book();
    try {
      book = BookMappers.convertToBook(bookRequestDto);
      bookRepository.save(book);
    }catch (Exception ex){
      ex.printStackTrace();
    }
    return book;
  }


  @Cacheable(value = "book")
  @Override
  public List<BookResponseDTO> listBooks() throws BusinessValidationException {
    List<BookResponseDTO> bookResponseDTOS;
      List<Book> bookList = bookRepository.findAll();
      bookResponseDTOS = bookList
             .stream()
              .filter(Objects::nonNull)
             .map(BookMappers::convertToBookResponse)
             .collect(Collectors.toList());

    return bookResponseDTOS;

  }

 @Cacheable(value = "book",key = "#id")
  @Override
  public BookResponseDTO getBook(Long id) throws BooknotFoundException {
      BookResponseDTO bookResponseDTO;
      Book book = bookRepository.findById(id)
              .orElseThrow(()->new BooknotFoundException(Translator.toLocale("bookstore.not.found")));
      bookResponseDTO = BookMappers.convertToBookResponse(book);

    return bookResponseDTO;

  }

  @Override
  public Book findByFileName(String fileName) {
    return bookRepository.findByFileName(fileName);
  }


}
