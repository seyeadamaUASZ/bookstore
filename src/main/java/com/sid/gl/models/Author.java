package com.sid.gl.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Author {
    private String name;
    private String contacts;
    private String resume;
}
