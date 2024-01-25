package com.sid.gl.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.dto.SearchRequestDTO;
import com.sid.gl.exceptions.BooknotFoundException;
import com.sid.gl.exceptions.BusinessValidationException;
import com.sid.gl.models.Book;
import com.sid.gl.queries.QuerySpecification;
import com.sid.gl.queries.SearchCriteria;
import com.sid.gl.queries.SearchOperation;
import com.sid.gl.repository.BookRepository;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.BookMappers;
import com.sid.gl.util.Translator;
import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

import static com.sid.gl.specifications.BookSpecification.*;

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


  //@Cacheable(value = "book")
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


  //TODO: first approche
    @Override
    public List<BookResponseDTO> searchByTitleOrIsbnOrBookType(SearchRequestDTO searchRequestDTO) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(searchRequestDTO,Map.class);
        List<Book> bookList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("key " + key);
            System.out.println("value " + value);
            if (StringUtils.isNotEmpty(key) && Objects.nonNull(value)) {
                switch (key) {
                    case "title":
                       bookList = bookRepository.findAll(Specification.where(hasTitle(value.toString())));
                        break;
                    case "isbn":
                       bookList = bookRepository.findAll(Specification.where(hasISBN(value.toString())));
                        break;

                    case "booktype":
                        bookList = bookRepository.findAll(Specification.where(containsBookType(value.toString())));
                        break;

                    default:
                        throw new IllegalArgumentException(Translator.toLocale("bookstore.search.error"));
                }
            }
        }

        return BookMappers.convertListBookResponse(bookList);
    }

   // another way search
    @Override
    public List<BookResponseDTO> searchBook(SearchRequestDTO dto) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(dto, Map.class);
        QuerySpecification<Book> bookSpecification = new QuerySpecification<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getKey())  && Objects.nonNull(entry.getValue()) ) {
                SearchCriteria searchCriteria;
                switch (entry.getKey()) {
                    case "title":
                        searchCriteria = new SearchCriteria("title", entry.getValue().toString(), SearchOperation.EQUAL);
                        break;
                    case "isbn":
                        searchCriteria = new SearchCriteria("isbn", entry.getValue().toString(), SearchOperation.EQUAL);
                        break;
                    case "bookType":
                        searchCriteria = new SearchCriteria("bookType", entry.getValue().toString(), SearchOperation.LIKE);
                        break;
                    default:
                        throw new IllegalArgumentException(Translator.toLocale("bookstore.search.error"));

                }

                bookSpecification.add(searchCriteria);
            }
        }

        return BookMappers.convertListBookResponse(bookRepository.findAll(bookSpecification));
    }


}
