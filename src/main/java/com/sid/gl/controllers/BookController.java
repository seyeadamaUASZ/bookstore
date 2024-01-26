package com.sid.gl.controllers;

import com.sid.gl.constants.ApiPath;
import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.dto.SearchRequestDTO;
import com.sid.gl.models.Book;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.ApiResponse;
import com.sid.gl.util.FileStorageService;
import com.sid.gl.util.JsonConverter;
import com.sid.gl.util.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.util.NamedFeature;

import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(ApiPath.API_V+"book")
@AllArgsConstructor
public class BookController {

    private final FeatureManager manager;

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
        if(!manager.isActive(CREATE_BOOK)){
            throw new IllegalArgumentException(Translator.toLocale("feature.not.activate"));
        }
        Book book = storageService.createBook(bookRequestDto,file);

        ApiResponse<Book> apiResponse=ApiResponse
                .<Book>builder()
                .status(SUCCESS)
                .results(book)
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

    @GetMapping(value="/downloadImageUri/{fileName}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public FileSystemResource getimfileimage(@PathVariable("fileName") String fileName) {
        Book book = iBookService.findByFileName(fileName);
        return new FileSystemResource(new File("./uploads/"+book.getFileName()));
    }

    //search using criteria

    @GetMapping("/search-book")
    public ResponseEntity<?> searchBookWithOptions(@Valid SearchRequestDTO searchRequestDTO){
        log.info("option search book {} .... ",searchRequestDTO);
        List<BookResponseDTO> list = iBookService.searchBook(searchRequestDTO);
        ApiResponse<List<BookResponseDTO>> responseDTO = ApiResponse
                .<List<BookResponseDTO>>builder()
                .status(SUCCESS)
                .results(list)
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
