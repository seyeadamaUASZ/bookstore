package com.sid.gl.dto;

import com.sid.gl.models.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private String description;
    private Author author;
    private String filebook;
    private String bookType;
    private String fileName;
}
