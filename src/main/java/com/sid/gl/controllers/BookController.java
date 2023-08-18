package com.sid.gl.controllers;

import com.sid.gl.constants.ApiPaths;
import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Book;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
@RequestMapping(ApiPaths.BASE_URL+ApiPaths.BOOK_URL)
@AllArgsConstructor
public class BookController {
    @Autowired
    private FeatureManager manager;
    public static final Feature CREATE_BOOK = new NamedFeature("CREATE_BOOK");
    private IBookService iBookService;
    public static final String SUCCESS = "Success";
    private FileStorageService storageService;


    @Operation( summary = "upload book and save",
            description = "save book",
            tags = { "book", "post" })
    @ApiResponses(
            {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Book.class), mediaType = "application/json") }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) }
    )
    @PostMapping(value="",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createBook(@RequestParam("bookDTO") String request, @RequestParam(value = "file")Optional<MultipartFile> file){
        BookRequestDto bookRequestDto = JsonConverter.convertToBookRequest(request);
        if(checkFlipNotActivate()){
            throw new RuntimeException(Translator.toLocale("feature.not.activate"));
        }
        Book book = storageService.createBook(bookRequestDto,file);
        //Design pattern Builder
        ApiResponse<Book> apiResponse=ApiResponse
                .<Book>builder()
                .status(SUCCESS)
                .results(book)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // create book another way using minio
    @Operation( summary = "upload book and save using s3 minio",
            description = "save book using s3 minio",
            tags = { "book", "post" })
    @ApiResponses(
            {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Book.class), mediaType = "application/json") }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) }
    )
    @SneakyThrows
    @PostMapping(value="/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestParam("bookDTO") String request, @RequestParam(value = "file")Optional<MultipartFile> file){
        if(checkFlipNotActivate()){
            throw new RuntimeException(Translator.toLocale("feature.not.activate"));
        }
        BookResponseDTO bookResult = iBookService.createBookWithFile(request,file);
        ApiResponse<BookResponseDTO> apiResponse=ApiResponse
                .<BookResponseDTO>builder()
                .status(SUCCESS)
                .results(bookResult)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @Operation( summary = "Get all books",
            description = "get all books",
            tags = { "book", "get" })
    @ApiResponses(
            {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Book.class), mediaType = "application/json") }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) }
    )
    @GetMapping
    public ResponseEntity<ApiResponse> getBooks() {
        List<BookResponseDTO> books = iBookService.listBooks();
        //Builder Design pattern (to avoid complex object creation headache)
        ApiResponse<List<BookResponseDTO>> responseDTO = ApiResponse
                .<List<BookResponseDTO>>builder()
                .status(SUCCESS)
                .results(books)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation( summary = "get book with id",
            description = "book id",
            tags = { "book", "get" })
    @ApiResponses(
            {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Book.class), mediaType = "application/json") }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) }
    )
    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable long bookId) {

        BookResponseDTO bookResponseDTO = iBookService.getBook(bookId);
        ApiResponse<BookResponseDTO> responseDTO = ApiResponse
                .<BookResponseDTO>builder()
                .status(SUCCESS)
                .results(bookResponseDTO)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation( summary = "get book with filename",
            description = "book filename",
            tags = { "book", "get" })
    @ApiResponses(
            {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Book.class), mediaType = "application/json") }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) }
    )
    @GetMapping(value="/link/{fileName}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public FileSystemResource getImfileimage(@PathVariable("fileName") String fileName) throws IOException {
        Book book = iBookService.findByFileName(fileName);
        return new FileSystemResource(new File("./uploads/"+book.getFileName()));
    }

    //get Object on minio bucket

    @Operation( summary = "get book with key stored on minio",
            description = "get book with key file",
            tags = { "book", "get" })
    @ApiResponses(
            {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Book.class), mediaType = "application/json") }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) }
    )
    @GetMapping(value = "/check/{key}" ,consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> check(@PathVariable(name = "key") String key){
       byte[] bytes = iBookService.getObject(key);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +key + "\"")
                .body(new ByteArrayResource(bytes));
    }

    private boolean checkFlipNotActivate(){
        return !manager.isActive(CREATE_BOOK);
    }
}
