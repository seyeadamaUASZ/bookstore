package com.sid.gl.controllers;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Book;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.ApiResponse;
import com.sid.gl.util.BookMappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
@Slf4j
public class BookController {

    private IBookService iBookService;

    public static final String SUCCESS = "Success";

    @PostMapping
    public ResponseEntity<ApiResponse> createBook(@Valid @RequestBody BookRequestDto bookRequestDto){
        log.info("BookController:createBook request body {}", BookMappers.jsonObjectToString(bookRequestDto));
        Book book = iBookService.addBook(bookRequestDto);
        //Design pattern Builder
        ApiResponse<Book> apiResponse=ApiResponse
                .<Book>builder()
                .status(SUCCESS)
                .results(book)
                .build();
        log.info("BookController::createBook response {}", BookMappers.jsonObjectToString(book));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getBooks() {

        List<BookResponseDTO> books = iBookService.listBooks();
        //Builder Design pattern (to avoid complex object creation headache)
        ApiResponse<List<BookResponseDTO>> responseDTO = ApiResponse
                .<List<BookResponseDTO>>builder()
                .status(SUCCESS)
                .results(books)
                .build();

        log.info("BookController::getBooks response {}", BookMappers.jsonObjectToString(responseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable long bookId) {

        log.info("BookController::getBook by id  {}", bookId);

        BookResponseDTO bookResponseDTO = iBookService.getBook(bookId);
        ApiResponse<BookResponseDTO> responseDTO = ApiResponse
                .<BookResponseDTO>builder()
                .status(SUCCESS)
                .results(bookResponseDTO)
                .build();

        log.info("ProductController::getProduct by id  {} response {}", bookId,BookMappers
                .jsonObjectToString(bookResponseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
