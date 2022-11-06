package com.sid.gl.dto;

import com.sid.gl.models.Author;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class BookRequestDto {
    private Long id;

    @NotBlank(message = "title not empty !!")
    private String title;
    @NotBlank(message = "isbn not empty")
    private String isbn;

    private String description;
    @NotEmpty(message = "author do not empty")
    private Author author;

    private String filebook;

    @NotBlank(message = "booktype must be not null")
    private String bookType;
}
