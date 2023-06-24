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

import org.springframework.stereotype.Service;

import java.util.List;

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

  @Override
  public List<BookResponseDTO> listBooks() throws BusinessValidationException {
    List<BookResponseDTO> bookResponseDTOS;
    try{
      List<Book> bookList = bookRepository.findAll();
      bookResponseDTOS = bookList
             .stream()
             .map(BookMappers::convertToBookResponse)
             .collect(Collectors.toList());

    }catch (Exception ex){
      throw new BusinessValidationException("exception for get all books");
    }
    return bookResponseDTOS;

  }

  @Override
  public BookResponseDTO getBook(Long id) throws BooknotFoundException {
    BookResponseDTO bookResponseDTO;
    try{
      Book book = bookRepository.findById(id)
              .orElseThrow(()->new BooknotFoundException("book id not found ...."));
      bookResponseDTO = BookMappers.convertToBookResponse(book);
    }catch(Exception ex){
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
