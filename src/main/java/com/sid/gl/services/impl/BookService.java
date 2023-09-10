package com.sid.gl.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.sid.gl.validators.DocumentValidator;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookService implements IBookService {
  private BookRepository bookRepository;
  @Autowired
  private MinioAdapter minioAdapter;
  @Autowired
  private RateLimiterRegistry registry;


  @PostConstruct
  public void postContruct(){
      io.github.resilience4j.ratelimiter.RateLimiter.EventPublisher eventPublisher = registry.rateLimiter("backendA").getEventPublisher();
      eventPublisher.onEvent(event -> log.info("Simple Rate Limit - On Event. Event Details: " + event));
      eventPublisher.onSuccess(event -> log.info("Simple Rate Limit - On Success. Event Details: " + event));
      eventPublisher.onFailure(event -> log.info("Simple Rate Limit - On Failure. Event Details: " + event));
  }

  @SneakyThrows
  @Override
  public Book addBook(BookRequestDto bookRequestDto) throws BusinessValidationException {
      return saveBook(bookRequestDto);
    }

    @RateLimiter(name = "backendA",fallbackMethod = "getResultFallbackMethod")
    @Cacheable(value = "books")
    @Override
  public List<BookResponseDTO> listBooks() throws BusinessValidationException {
    List<BookResponseDTO> bookResponseDTOS;
      List<Book> bookList = bookRepository.findAll();
      bookResponseDTOS = bookList
             .stream()
              .filter(Objects::nonNull)
             .map(BookMappers::convertToBookResponse)
             .collect(Collectors.toList());
    log.info("retrieve all books");
    return bookResponseDTOS;

  }

  private List<BookResponseDTO> getResultFallbackMethod(RequestNotPermitted requestNotPermitted){
      log.info("fail request api calling");
      log.info("RequestNotPermitted exception message: {}", requestNotPermitted.getMessage());
      return Collections.emptyList();
  }

 @Cacheable(value = "book")
  @Override
  public BookResponseDTO getBook(Long id) throws BooknotFoundException {
      BookResponseDTO bookResponseDTO;
      Book book = bookRepository.findById(id)
              .orElseThrow(()->new BooknotFoundException(Translator.toLocale("bookstore.not.found")));
      bookResponseDTO = BookMappers.convertToBookResponse(book);
    log.info("get one book with id : {} ",id);
    return bookResponseDTO;

  }

  @Override
  public Book findByFileName(String fileName) {
    return bookRepository.findByFileName(fileName);
  }

    @SneakyThrows
    @Override
    public BookResponseDTO createBookWithFile(String request, Optional<MultipartFile> file)  {
       log.info("create book with body {} ",request);
        BookRequestDto bookRequestDto = JsonConverter.convertToBookRequest(request);

        Book book = saveBook(bookRequestDto);

        //check file is empty or not
        if(file.isPresent()){
            //upload file on minio
            //validate document
            MultipartFile fileChedked = file.get();
            boolean checked = DocumentValidator.validateDocument(fileChedked);
           if(checked){
               String key= minioAdapter.uploadFile(fileChedked);
               book.setFileKey(key);
               bookRepository.save(book);
           }else{
               throw new BusinessValidationException(Translator.toLocale("document.not.valid"));
           }
        }
        return BookMappers.convertToBookResponse(book);
    }

    @Override
    public byte[] getObject(String key) {
        return minioAdapter.getObject(key);
    }

    private Book saveBook(BookRequestDto bookRequestDto) throws JsonProcessingException {
      log.info("save book with body -- {} ", JsonConverter.convertToString(bookRequestDto));
      Book book= BookMappers.convertToBook(bookRequestDto);
      return bookRepository.save(book);
    }



}
