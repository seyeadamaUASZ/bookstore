package com.sid.gl.controllers;

import com.sid.gl.advisors.BookServiceExceptionHandler;
import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Author;
import com.sid.gl.models.Book;
import com.sid.gl.services.IBookService;
import com.sid.gl.util.BookMappers;
import com.sid.gl.util.FileStorageService;
import com.sid.gl.util.JsonConverter;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private IBookService iBookService;
    @InjectMocks
    private BookController bookController;

    @Mock
    private FileStorageService storageService;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new BookServiceExceptionHandler())
                .build();
    }

    @Test
    void createBook() throws Exception {
        BookRequestDto bookRequestDto = buildBookRequest();
        Book book = BookMappers.convertToBook(bookRequestDto);

        Mockito.when(storageService.createBook(bookRequestDto, Optional.empty())).thenReturn(book);
        ResultActions ra = mvc.perform(
                post("/api/v1/book").contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("bookDTO",JsonConverter.asJsonString(bookRequestDto))
        );
        // then
        ra.andExpect(status().is(org.springframework.http.HttpStatus.CREATED.value()));
    }

    @Test
    void getBooks() throws Exception {
        BookResponseDTO bookResponseDTO =buildBookResponse();

        Mockito.when(iBookService.listBooks()).thenReturn(Collections.singletonList(bookResponseDTO));
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get("/api/v1/book")
                        .contentType("application/json"));

        MvcResult result = ra.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        ra.andExpect(status().is(HttpStatus.SC_OK));
        ra.andExpect(status().is(HttpStatus.SC_OK));
        assertTrue(contentAsString.contains("\"title\":\"title\""));
        assertTrue(contentAsString.contains("\"isbn\":\"isbn\""));
        assertTrue(contentAsString.contains("\"description\":\"description\""));
    }

    @Test
    void getBook() throws Exception {
        BookResponseDTO response = buildBookResponse();
        Mockito.when(iBookService.getBook(1L)).thenReturn(response);
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get("/api/v1/book/1")
                        .contentType("application/json"));
        MvcResult result = ra.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        ra.andExpect(status().is(HttpStatus.SC_OK));
        ra.andExpect(status().is(HttpStatus.SC_OK));
        assertTrue(contentAsString.contains("\"title\":\"title\""));
        assertTrue(contentAsString.contains("\"isbn\":\"isbn\""));
        assertTrue(contentAsString.contains("\"description\":\"description\""));
    }

    private BookResponseDTO buildBookResponse(){
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setBookType("type");
        bookResponseDTO.setId(1L);
        bookResponseDTO.setTitle("title");
        bookResponseDTO.setDescription("description");
        bookResponseDTO.setAuthor(new Author());
        bookResponseDTO.setIsbn("isbn");
        return bookResponseDTO;
    }

    private BookRequestDto buildBookRequest(){
        BookRequestDto book = new BookRequestDto();
        book.setTitle("title");
        book.setBookType("ROMAN");
        book.setId(2L);
        book.setDescription("description");
        Author author = new Author();
        author.setContacts("contacts");
        author.setResume("resume");
        author.setName("author");
        book.setAuthor(author);
        return book;
    }
}