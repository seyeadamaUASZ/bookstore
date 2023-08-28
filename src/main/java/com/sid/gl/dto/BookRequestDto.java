package com.sid.gl.dto;

import com.sid.gl.models.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




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
    @NotBlank(message = "booktype must be not null")
    private String bookType;

}
