package com.sid.gl.controllers;

import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Book;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.ApiResponse;
import com.sid.gl.util.FileStorageService;
import com.sid.gl.util.JsonConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.util.NamedFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {
    @Autowired
    private FeatureManager manager;

    public static final Feature CREATE_BOOK = new NamedFeature("CREATE_BOOK");

    private IBookService iBookService;

    public static final String SUCCESS = "Success";

    private FileStorageService storageService;

    @PostMapping(value="",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createBook(@RequestParam("bookDTO") String request, @RequestParam(value = "file")Optional<MultipartFile> file){
        BookRequestDto bookRequestDto = JsonConverter.convertToBookRequest(request);
        //log.info("BookController:createBook request body {}", BookMappers.jsonObjectToString(bookRequestDto));
        if(!manager.isActive(CREATE_BOOK)){
            throw new RuntimeException("This fonctionality is not active");
        }
        Book book = storageService.createBook(bookRequestDto,file);
        //Design pattern Builder
        ApiResponse<Book> apiResponse=ApiResponse
                .<Book>builder()
                .status(SUCCESS)
                .results(book)
                .build();
        //log.info("BookController::createBook response {}", BookMappers.jsonObjectToString(book));
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

        //log.info("BookController::getBooks response {}", BookMappers.jsonObjectToString(responseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable long bookId) {

        //log.info("BookController::getBook by id  {}", bookId);

        BookResponseDTO bookResponseDTO = iBookService.getBook(bookId);
        ApiResponse<BookResponseDTO> responseDTO = ApiResponse
                .<BookResponseDTO>builder()
                .status(SUCCESS)
                .results(bookResponseDTO)
                .build();

        //log.info("BookController::getProduct by id  {} response {}", bookId,BookMappers
             //   .jsonObjectToString(bookResponseDTO));

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value="/link/{fileName}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public FileSystemResource getimfileimage(@PathVariable("fileName") String fileName) throws IOException {
        Book book = iBookService.findByFileName(fileName);
        return new FileSystemResource(new File("./uploads/"+book.getFileName()));
    }

}
