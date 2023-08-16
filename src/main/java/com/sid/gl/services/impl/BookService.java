package com.sid.gl.services.impl;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.exceptions.BooknotFoundException;
import com.sid.gl.exceptions.BusinessValidationException;
import com.sid.gl.models.Book;
import com.sid.gl.repository.BookRepository;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.BookMappers;
import com.sid.gl.util.JsonConverter;
import com.sid.gl.util.Translator;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService implements IBookService {
  private BookRepository bookRepository;
  @Autowired
  private MinioAdapter minioAdapter;

  @Override
  public Book addBook(BookRequestDto bookRequestDto) throws BusinessValidationException {
      return saveBook(bookRequestDto);
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

    @Override
    public Book createBookWithFile(String request, Optional<MultipartFile> file)  {
        BookRequestDto bookRequestDto = JsonConverter.convertToBookRequest(request);

        Book book = saveBook(bookRequestDto);

        //check file is empty or not
        if(file.isPresent()){
            //upload file on minio
            MultipartFile fileChedked = file.get();
            String key= minioAdapter.uploadFile(fileChedked);
            //update
            book.setFileKey(key);
            bookRepository.save(book);
        }
        return book;
    }

    @Override
    public byte[] getObject(String key) {
        return minioAdapter.getObject(key);
    }

    private Book saveBook(BookRequestDto bookRequestDto){
      Book book= BookMappers.convertToBook(bookRequestDto);
      return bookRepository.save(book);
    }



}
