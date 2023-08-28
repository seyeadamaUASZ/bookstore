package com.sid.gl.integrations;

import com.sid.gl.BookstoreApplication;
import com.sid.gl.dto.BookRequestDto;
import com.sid.gl.dto.BookResponseDTO;
import com.sid.gl.models.Author;

import com.sid.gl.util.ApiResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookIntTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;

    private String getRootUrl(){
        return "http://localhost:"+port+"/api/v1";
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void testAllBooks(){
        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ApiResponse> response = restTemplate.exchange(getRootUrl()+"/book",
                HttpMethod.GET, entity,ApiResponse.class);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals("Success",response.getBody().getStatus());

    }


    @Test
    public void testGetBookById() {
        ApiResponse<BookResponseDTO> response = restTemplate.getForObject(getRootUrl() + "/book/1", ApiResponse.class);
        Assert.assertNotNull(response);
    }

    @Test
    public void testCreateBook() {
        BookRequestDto book = new BookRequestDto();
        //book.setFileKey("filekey");
        book.setTitle("title");
        book.setBookType("ROMAN");
        book.setId(2L);
        book.setDescription("description");
        Author author = new Author();
        author.setContacts("contacts");
        author.setResume("resume");
        author.setName("author");
        book.setAuthor(author);

        ResponseEntity<ApiResponse> postResponse = restTemplate.postForEntity(getRootUrl() + "/book", book, ApiResponse.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
    }


}
