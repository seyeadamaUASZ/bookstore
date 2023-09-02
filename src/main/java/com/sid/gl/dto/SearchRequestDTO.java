package com.sid.gl.dto;

import lombok.Data;

@Data
public class SearchRequestDTO {
    private String title;
    private String isbn;
    private String bookType;
}
