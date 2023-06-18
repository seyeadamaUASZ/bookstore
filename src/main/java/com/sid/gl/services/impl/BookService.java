package com.sid.gl.services.impl;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.exceptions.BooknotFoundException;
import com.sid.gl.exceptions.BusinessValidationException;
import com.sid.gl.models.Book;
import com.sid.gl.repository.BookRepository;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.BookMappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@Slf4j
@AllArgsConstructor
public class BookService implements IBookService {
  private BookRepository bookRepository;


  @Override
  public Book addBook(BookRequestDto bookRequestDto) throws BusinessValidationException {
    Book book=new Book();
    try {
      //log.info("bookservice:addBook executed start...");
      book = BookMappers.convertToBook(bookRequestDto);
      //log.debug("bookservice request body ...{}",BookMappers.jsonObjectToString(bookRequestDto));
      bookRepository.save(book);
    }catch (Exception ex){
      //log.error("BookService:addBook exception occured message {}",ex.getMessage());
    }
    return book;
  }

  @Override
  public List<BookResponseDTO> listBooks() throws BusinessValidationException {
    List<BookResponseDTO> bookResponseDTOS;
    try{
      //log.info("call method to list all books");
      List<Book> bookList = bookRepository.findAll();
      bookResponseDTOS = bookList
             .stream()
             .map(BookMappers::convertToBookResponse)
             .collect(Collectors.toList());
      //log.debug("all books are retrieving from database");
    }catch (Exception ex){
      //log.error("Exception occured to retrieve all books {}",ex.getMessage());
      throw new BusinessValidationException("exception for get all books");
    }
    //log.info("get successfully all books from database");
    return bookResponseDTOS;

  }

  @Override
  public BookResponseDTO getBook(Long id) throws BooknotFoundException {
    BookResponseDTO bookResponseDTO;
    try{
      //log.info("Bookservice:getBook execution started.");
      Book book = bookRepository.findById(id)
              .orElseThrow(()->new BooknotFoundException("book id not found ...."));
      bookResponseDTO = BookMappers.convertToBookResponse(book);
    }catch(Exception ex){
      //log.error("Exception occured book id not found",ex.getMessage());
      throw new BooknotFoundException("Book id not found ");
    }

    return bookResponseDTO;

  }

  @Override
  public Book findByFileName(String fileName) {
    return bookRepository.findByFileName(fileName);
  }

  //other implementation using map
  public BookResponseDTO getOneBook(Long id){
     return Optional
             .of(bookRepository.findById(id))
             .filter(Optional::isPresent)
             .map(Optional::get)
             .map(BookMappers::convertToBookResponse)
             .map(BookResponseDTO::new)
             .get();
  }

}
