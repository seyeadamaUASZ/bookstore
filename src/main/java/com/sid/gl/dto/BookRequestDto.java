package com.sid.gl.dto;

import com.sid.gl.models.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {
    private Long id;

    @NotBlank(message = "title not empty !!")
    private String title;
    @NotBlank(message = "isbn not empty")
    private String isbn;

    private String description;

    @NotNull(message = "Author info must be given")
    private Author author;

    private String filebook;

    @NotBlank(message = "booktype must be not null")
    private String bookType;

    private String fileName;
}
